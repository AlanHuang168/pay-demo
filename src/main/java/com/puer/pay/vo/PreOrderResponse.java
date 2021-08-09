package com.puer.pay.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: Alan
 * @date: 2021-06-10 20:59
 */
@Data
@Accessors(chain = true)
public class PreOrderResponse {

    private String trade_id;

    private int actual_fee;

    private String body;

    private String mch_trade_id;

    private String merchant_id;

    private String pay_type;

    private String platform_merchant_id;

    private int settle_fee;

    private int total_fee;

    private int trade_fee;

    private String pay_info;

    private int trade_state;

    private String trade_info;

    private PayInfoVo payInfoVo;
}
