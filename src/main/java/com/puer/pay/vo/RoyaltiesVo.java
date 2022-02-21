package com.puer.pay.vo;

import lombok.Data;

/**
 * @author shenggongjie
 * @date 2022/2/21 10:52
 */
@Data
public class RoyaltiesVo {

    private Boolean is_originator;
    private Boolean is_trade_fee;
    private Integer royalty_fee;
    private Long trans_in;
}
