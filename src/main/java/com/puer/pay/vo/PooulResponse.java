package com.puer.pay.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Alan
 * @date: 2021-06-10 20:58
 */
@Data
@Accessors(chain = true)
public class PooulResponse<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    public boolean isOk(){
        return this.code == 0;
    }

}
