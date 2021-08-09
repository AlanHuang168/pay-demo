package com.puer.pay.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shenggongjie
 * @date 2021/7/8 17:08
 */
@NoArgsConstructor
@Data
public class PayCreateChildVo {


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
        private String _id;
        @JsonProperty("cmbc_info")
        private CmbcInfoDTO cmbc_info;

        @NoArgsConstructor
        @Data
        public static class CmbcInfoDTO {
            @JsonProperty("fundAccName")
            private String fundAccName;
            @JsonProperty("fundAcc")
            private String fundAcc;
            @JsonProperty("ecp_bank_branch")
            private String ecp_bank_branch;
        }
    }
}
