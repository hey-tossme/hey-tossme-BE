package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.dto.ItemDto.CreateItemRequest;
import com.blackdragon.heytossme.dto.ItemDto.Response;
import com.blackdragon.heytossme.dto.Kakao;
import com.blackdragon.heytossme.dto.Kakao.AddressInfo;
import com.blackdragon.heytossme.exception.CustomException;
import com.blackdragon.heytossme.exception.ErrorCode;
import com.blackdragon.heytossme.persist.ItemRepository;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.Address;
import com.blackdragon.heytossme.persist.entity.Item;
import com.blackdragon.heytossme.type.ItemStatus;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${com.blackdragon.kakao.key}")
    private String API_KEY;

    private AddressInfo getData(String address) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", API_KEY_PREFIX + API_KEY);
        String addressURL = "https://dapi.kakao.com/v2/local/search/address.json";
        URI targetURI = UriComponentsBuilder.fromHttpUrl(addressURL)
                .queryParam("query", address)
//                .queryParam("analyze_type", "exact");
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
        try {
            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

            RestTemplate restTemplate = new RestTemplate();
            Kakao result = restTemplate.exchange(targetURI, HttpMethod.GET, entity, Kakao.class)
                    .getBody();
            return result.getDocuments().get(0).getAddress();

        } catch (Exception e) {
            throw new CustomException(ErrorCode.ADDRESS_NOT_FOUND);
        }
    }

    public Response createItem(CreateItemRequest request) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime dueDate = LocalDateTime.parse(request.getDueDate(), dateFormatter);
//        log.info("dueDate = {}", dueDate);
        AddressInfo addressInfo = getData(request.getAddress());
//        log.info("address = {}", address);
        Item item = Item.builder()
                .member(memberRepository.findById(request.getSellerId())
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)))
                .category(request.getCategory())
                .title(request.getTitle())
                .contents(request.getContents())
                .dueDate(dueDate)
                .latitude(addressInfo.getY())
                .longitude(addressInfo.getX())
                .imageUrl(request.getImageUrl())
                .status(ItemStatus.SALE.name())
                .build();
        Address address = Address.builder()
                .item(item)
                .sidoArea(addressInfo.getRegion_1depth_name())
                .sigunArea(addressInfo.getRegion_2depth_name())
                .lotRoadAddress(addressInfo.getRegion_3depth_name())
                .detailAddress(addressInfo.getAddress_name()
                        .substring(addressInfo.getRegion_1depth_name().length()).trim()
                        .substring(addressInfo.getRegion_2depth_name().length()).trim()
                        .substring(addressInfo.getRegion_3depth_name().length()).trim()
                        + " " + request.getAddressDetail())
                .build();
        item.setAddress(address);
        itemRepository.save(item);
        return new Response(item);
    }
}
