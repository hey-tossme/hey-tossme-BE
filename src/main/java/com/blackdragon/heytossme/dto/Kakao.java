package com.blackdragon.heytossme.dto;

import java.util.List;
import lombok.Data;

@Data
public class Kakao {

    private Object meta;
    private List<AddressList> documents;

    @Data
    public static class AddressList {

        private String address_name;
        private float x;
        private float y;
        private String address_type;
        private AddressInfo address;
        private Object road_address;
    }

    @Data
    public static class AddressInfo {

        private String address_name;
        private String region_1depth_name;
        private String region_2depth_name;
        private String region_3depth_name;
        private String region_3depth_h_name;
        private Long h_code;
        private Long b_code;
        private String mountain_yn;
        private Integer main_address_no;
        private String sub_address_no;
        private float x;
        private float y;
    }
}
