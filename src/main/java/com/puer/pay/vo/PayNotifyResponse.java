package com.puer.pay.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dozer.Mapping;

/**
 * @Description:
 * @Author: Alan
 * @date: 2021-06-15 11:49
 */
@Data
@Accessors(chain = true)
public class PayNotifyResponse {

    //普尔平台单号
    private String trade_id;

    //商户订单号
    private String mch_trade_id;

    //上游业务订单号
    private String out_trade_id;

    //wechat.jsapi/微信;alipay.scan/支付宝
    private String pay_type;

    //商品或支付单简要描述
    private String body;

    //支付总金额
    private Integer total_fee;

    //结算金额
    private Integer settle_fee;

    //手续费金额
    private Integer trade_fee;

    //手续费金额
    private String trade_info;

    //附加数据
    private String attach;

    //商户门店编号
    private String store_id;

    //发起支付的终端IP，
    private String spbill_create_ip;

    //微信实名后openid
    private String openid;

    //支付宝实名后buyer_id
    private String buyer_id;

    //实名后用户信息
    private String user_info;

}
