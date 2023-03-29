package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.dto.ItemDto.DealListResponse;
import com.blackdragon.heytossme.dto.ItemDto.ItemRequest;
import com.blackdragon.heytossme.dto.ItemDto.Response;
import com.blackdragon.heytossme.dto.Kakao;
import com.blackdragon.heytossme.dto.Kakao.AddressInfo;
import com.blackdragon.heytossme.exception.AuthException;
import com.blackdragon.heytossme.exception.ItemException;
import com.blackdragon.heytossme.exception.errorcode.AuthErrorCode;
import com.blackdragon.heytossme.exception.errorcode.ItemErrorCode;
import com.blackdragon.heytossme.persist.AddressRepository;
import com.blackdragon.heytossme.persist.HistoryRepository;
import com.blackdragon.heytossme.persist.ItemRepository;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.Address;
import com.blackdragon.heytossme.persist.entity.History;
import com.blackdragon.heytossme.persist.entity.Item;
import com.blackdragon.heytossme.persist.entity.Member;
import com.blackdragon.heytossme.type.Category;
import com.blackdragon.heytossme.type.ItemStatus;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private static final String API_KEY_PREFIX = "KakaoAK ";
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final AddressRepository addressRepository;

    @Value("${com.blackdragon.kakao.key}")
    private String apiKey;

    public Response createItem(Long sellerId, ItemRequest request) {
        Item item = setItemEntityFromRequest(sellerId, request);
        itemRepository.save(item);

        return new Response(item);
    }

    public Page<Response> getList(Integer pageNum, Integer size, String region, String startDue,
            String endDue, String searchTitle, String category) {
        Pageable pageable = PageRequest.of(pageNum == null ? 0 : pageNum, size == null ? 8 : size);
        Specification<Item> search = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("status"), ItemStatus.SALE)); // 판매 중인 상품
            if (StringUtils.isNotBlank(searchTitle)) {
                predicates.add(cb.like(root.get("title"), "%" + searchTitle + "%"));
            }
            if (StringUtils.isNotBlank(endDue)) {
                LocalDateTime endDueDate = parseToDateType(endDue);
                predicates.add(cb.between(root.get("dueDate"),
                        StringUtils.isBlank(startDue) ? endDueDate : parseToDateType(startDue),
                        endDueDate.withHour(23).withMinute(59).withSecond(59)));
            }
            if (StringUtils.isNotBlank(category)) {
                predicates.add(
                        cb.equal(root.get("category"), Category.valueOf(category.toUpperCase())));
            }
            if (StringUtils.isNotBlank(region)) {
                Join<Item, Address> itemAddressJoin = root.join("address");
                String[] address = region.split(" ");
                predicates.add(cb.equal(itemAddressJoin.get("sidoArea"), address[0]));
                predicates.add(cb.equal(itemAddressJoin.get("sigunArea"), address[1]));
            }

            Predicate[] p = new Predicate[predicates.size()];
            return cb.and(predicates.toArray(p));
        };
        Page<Item> listPage = itemRepository.findAll(search, pageable);

        return listPage.map(Response::new);
    }

    @Transactional
    public Response modify(Long itemId, Long sellerId, ItemRequest request) {
        Item item = findItemById(itemId);
        Address address = item.getAddress();

        if (!item.getSeller().getId().equals(sellerId)) {
            throw new ItemException(ItemErrorCode.SELLER_MISMATCH);
        }
        if (StringUtils.isNotBlank(request.getCategory())) {
            item.setCategory(Category.findBy(request.getCategory()));
        }
        if (StringUtils.isNotBlank(request.getTitle())) {
            item.setTitle(request.getTitle());
        }
        if (request.getPrice() != null && request.getPrice() != 0) {
            item.setPrice(request.getPrice());
        }
        if (StringUtils.isNotBlank(request.getDueDate())) {
            LocalDateTime dueDate = parseToDateType(request.getDueDate());
            item.setDueDate(dueDate);
        }
        if (StringUtils.isNotBlank(request.getContents())) {
            item.setContents(request.getContents());
        }
        if (StringUtils.isNotBlank(request.getAddress())) {
            AddressInfo addressInfo = getAddressData(request.getAddress());
            address.setFirstDepthRegion(addressInfo.getRegion_1depth_name());
            address.setSecondDepthRegion(addressInfo.getRegion_2depth_name());
            address.setThirdDepthRegion(addressInfo.getRegion_3depth_name());
            address.setFirstDetailAddress(getFirstDetailAddressFromAddressInfo(addressInfo));
        }
        if (StringUtils.isNotBlank(request.getAddressDetail())) {
            address.setSecondDetailAddress(request.getAddressDetail());
        }
        if (StringUtils.isNotBlank(request.getImageUrl())) {
            item.setImageUrl(request.getImageUrl());
        }

        item.setAddress(address);
        itemRepository.save(item);

        return new Response(item);
    }

    public Response getDetail(Long itemId) {
        Item item = findItemById(itemId);

        return new Response(item);
    }

    @Transactional
    public void deleteItem(Long itemId, Long sellerId) {
        Item item = findItemById(itemId);

        if (!item.getSeller().getId().equals(sellerId)) {
            throw new ItemException(ItemErrorCode.SELLER_MISMATCH);
        }

        item.setStatus(ItemStatus.HIDDEN);
    }

    @Transactional
    public Response dealConfirm(Long itemId, Long sellerId, Long buyerId) {
        Item item = findItemById(itemId);
        if (!item.getSeller().getId().equals(sellerId)) {
            throw new ItemException(ItemErrorCode.SELLER_MISMATCH);
        }

        item.setStatus(ItemStatus.DONE);
        historyRepository.save(History.builder()
                .item(item)
                .buyer(findMember(buyerId))
                .build());
        return new Response(item);
    }

    public DealListResponse getBuyList(Long memberId, Integer pageNum, Integer size) {
        Page<Response> buyList = historyRepository.findAllByBuyerId(memberId,
                        PageRequest.of(pageNum == null ? 0 : pageNum, size == null ? 2 : size))
                .map(e -> new Response(e.getItem()));

        return new DealListResponse(buyList);
    }

    public DealListResponse getSellList(Long memberId, Integer pageNum, Integer size) {
        Page<Response> saleList = itemRepository.findAllBySellerId(memberId,
                        PageRequest.of(pageNum == null ? 0 : pageNum, size == null ? 4 : size))
                .map(Response::new);

        return new DealListResponse(saleList);
    }

    public HashMap<String, HashSet<String>> getAddressList() {
        var list = addressRepository.findDistinct();
        HashMap<String, HashSet<String>> addressMap = new HashMap<>();
        for (List<String> item : list) {
            HashSet<String> sigunList = addressMap.getOrDefault(item.get(0), new HashSet<>());
            sigunList.add(item.get(1).split(" ")[0]);
            addressMap.put(item.get(0), sigunList);
        }

        log.info("addressMap = {}", addressMap);

        return addressMap;
    }

    private Item setItemEntityFromRequest(Long sellerId, ItemRequest request) {
        LocalDateTime dueDate = parseToDateType(request.getDueDate());
        AddressInfo addressInfo = getAddressData(request.getAddress());

        Item item = Item.builder()
                .seller(findMember(sellerId))
                .category(Category.findBy(request.getCategory()))
                .title(request.getTitle())
                .contents(request.getContents())
                .price(request.getPrice())
                .dueDate(dueDate)
                .latitude(addressInfo.getY())
                .longitude(addressInfo.getX())
                .imageUrl(request.getImageUrl())
                .status(ItemStatus.SALE)
                .build();

        Address address = setAddressEntityFromRequest(item, request.getAddress(),
                request.getAddressDetail());

        item.setAddress(address);

        return item;
    }

    private Address setAddressEntityFromRequest(Item item, String addressFromRequest,
            String detailAddressFromRequest) {
        AddressInfo addressInfo = getAddressData(addressFromRequest);
        return Address.builder()
                .item(item)
                .firstDepthRegion(addressInfo.getRegion_1depth_name())
                .secondDepthRegion(addressInfo.getRegion_2depth_name())
                .thirdDepthRegion(addressInfo.getRegion_3depth_name())
                .firstDetailAddress(getFirstDetailAddressFromAddressInfo(addressInfo))
                .secondDetailAddress(detailAddressFromRequest)
                .build();
    }

    private String getFirstDetailAddressFromAddressInfo(AddressInfo addressInfo) {
        return addressInfo.getAddress_name()
                .substring(addressInfo.getRegion_1depth_name().length()).trim()
                .substring(addressInfo.getRegion_2depth_name().length()).trim()
                .substring(addressInfo.getRegion_3depth_name().length()).trim();
    }

    private AddressInfo getAddressData(String address) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", API_KEY_PREFIX + this.apiKey);
        String addressURL = "https://dapi.kakao.com/v2/local/search/address.json";
        URI targetURI = UriComponentsBuilder.fromHttpUrl(addressURL)
                .queryParam("query", address)
                .queryParam("analyze_type", "exact")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
        try {
            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

            RestTemplate restTemplate = new RestTemplate();
            Kakao result = restTemplate.exchange(targetURI, HttpMethod.GET, entity, Kakao.class)
                    .getBody();
            assert result != null;
            return result.getDocuments().get(0).getAddress();

        } catch (Exception e) {
            throw new ItemException(ItemErrorCode.ADDRESS_NOT_FOUND);
        }
    }

    private LocalDateTime parseToDateType(String stringDate) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        return LocalDateTime.parse(stringDate, dateFormatter);
    }

    private Member findMember(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));
    }
}
