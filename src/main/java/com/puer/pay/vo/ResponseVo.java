package com.puer.pay.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author shenggongjie
 * @date 2021/7/6 10:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseVo extends PayInfoVo{

    private String message;

    private Integer code;
}
