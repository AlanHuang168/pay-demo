package com.puer.pay.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shenggongjie
 * @date 2021/7/8 15:55
 */
@NoArgsConstructor
@Data
public class PayLoginVo {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("_id")
        private Integer _id;
        @JsonProperty("_type")
        private String _type;
        @JsonProperty("curr_merchant_id")
        private String curr_merchant_id;
    }
}
