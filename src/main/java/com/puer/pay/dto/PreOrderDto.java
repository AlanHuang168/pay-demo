package com.puer.pay.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author shenggongjie
 * @date 2021/7/7 11:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PreOrderDto extends MerchUrlDto {

    private String mch_pre_id;
    private BigDecimal money;
    private Integer total_fee;
    private String notify_url;
    private String body;
    private String merchId;
}
