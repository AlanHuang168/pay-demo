package com.puer.pay.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: Alan
 * @date: 2021-06-11 14:58
 */
@Data
public class PayInfoVo {

    /**
     * appid
     */
    private String appId;

    /**
     * 时间戳
     */
    private String timeStamp;

    /**
     * 随机串
     */
    private String nonceStr;

    /**
     * 签名类型
     */
    private String signType;

    /**
     * 签名
     */
    private String paySign;

    @JsonProperty("package")
    @JSONField(serialize = false)
    private String packageStr;

    /**
     * payPackage
     */
    private String payPackage;
}
