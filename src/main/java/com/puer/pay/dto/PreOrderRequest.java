package com.puer.pay.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: Alan
 * @date: 2021-06-11 14:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PreOrderRequest extends MerchUrlDto {

    private String pay_type;
    private String mch_trade_id;
    private Integer total_fee;
    private String body;
    private String sub_appid;
    private String sub_openid;
    private String nonce_str;
    private String notify_url;
    private String spbill_create_ip;
    private String store_id;
    private String device_info;
    private String op_user_id;
    private String attach;
}
