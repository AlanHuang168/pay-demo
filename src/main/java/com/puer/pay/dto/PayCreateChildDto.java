package com.puer.pay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shenggongjie
 * @date 2021/7/8 17:07
 */
@NoArgsConstructor
@Data
public class PayCreateChildDto {
    private String authorization;
    private String parent_id;
    private Integer license_type;
    private BusinessDTO business;
    private CorporateDTO corporate;
    private OwnerDTO owner;

    @NoArgsConstructor
    @Data
    public static class BusinessDTO {
        private String short_name;
        private String province;
        private String urbn;
        private String area;
        private String address;
    }

    @NoArgsConstructor
    @Data
    public static class CorporateDTO {
        private String full_name;
        private String license_num;
    }

    @NoArgsConstructor
    @Data
    public static class OwnerDTO {
        private Integer idcard_type;
        private String name;
    }
}
