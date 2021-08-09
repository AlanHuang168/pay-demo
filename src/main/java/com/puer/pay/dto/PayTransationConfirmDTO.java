package com.puer.pay.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * @Description:
 * @Author: Alan
 * @date: 2021-06-11 11:34
 */
@Data
public class PayTransationConfirmDTO{

    @NotBlank(message = "支付流水号不能为空")
    private String number;

    private Integer userId;

    private String token;

    private String merchId;

    private String openId;

    @NotBlank(message = "小程序appid不能为空")
    private String subAppid;

    private BigDecimal paidAmount;
}
