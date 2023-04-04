package com.blackdragon.heytossme.dto;

import com.blackdragon.heytossme.persist.entity.Address;
import com.blackdragon.heytossme.persist.entity.Item;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.domain.Page;

public class ItemDto {

    @Data
    public static class ItemRequest {

        private String category;
        private String title;
        private Integer price;
        private String dueDate;
        private String contents;
        private String address;
        private String addressDetail;
        private String imageUrl;
    }

    @Data
    public static class Response {

        private Long id;
        private MemberDto.Response seller;
        private String category;
        private String title;
        private String contents;
        private int price;
        private String address;
        private String addressDetail;
        private LocalDateTime createdAt;
        private LocalDateTime dueTime;
        private float latitude;
        private float longitude;
        private String imageUrl;
        private String status;

        public Response(Item item) {
            this.id = item.getId();
            this.seller = new MemberDto.Response(item.getSeller());
            this.category = item.getCategory().getToRealName();
            this.title = item.getTitle();
            this.contents = item.getContents();
            this.price = item.getPrice();
            this.address = convertAddress(item.getAddress());
            this.addressDetail = item.getAddress().getSecondDetailAddress();
            this.createdAt = item.getCreatedAt();
            this.dueTime = item.getDueDate();
            this.latitude = item.getLatitude();
            this.longitude = item.getLongitude();
            this.imageUrl = item.getImageUrl();
            this.status = item.getStatus().name();
        }

        public String convertAddress(Address address) {

            return address.getFirstDepthRegion() + " "
                    + address.getSecondDepthRegion()
                    + " "
                    + address.getThirdDepthRegion()
                    + " "
                    + address.getFirstDetailAddress();
        }
    }

    @Data
    public static class DealListResponse {

        private Page<Response> list;

        public DealListResponse(Page<Response> list) {
            this.list = list;
        }
    }
}
