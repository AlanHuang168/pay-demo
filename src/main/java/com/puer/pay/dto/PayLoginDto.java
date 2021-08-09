package com.puer.pay.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author shenggongjie
 * @date 2021/7/8 15:21
 */
@Data
public class PayLoginDto {

    private String login_name;
    private String password;
}
