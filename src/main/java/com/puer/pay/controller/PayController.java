package com.puer.pay.controller;

import com.puer.pay.dto.*;
import com.puer.pay.service.PayService;
import com.puer.pay.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author shenggongjie
 * @date 2021/7/2 14:36
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/pay")
@CrossOrigin
public class PayController {

    private final PayService payService;

    /**
     * 登陆
     * @param dto
     * @return
     */
    @PostMapping("/payLogin")
    public PayLoginVo payLogin(@RequestBody PayLoginDto dto, HttpServletResponse response){
        return payService.payLogin(dto, response);
    }

    /**
     * 创建入驻商户   如果请求头里面有值，就先去请求头里面的authorization 其次取body里面的authorization
     * @return
     */
    @PostMapping("/payCreateChild")
    public PayCreateChildVo payCreateChild(@RequestBody PayCreateChildDto dto,HttpServletRequest request){
        dto.setAuthorization(request.getHeader("Authorization"));
        return payService.payCreateChild(dto);
    }

    /**
     * 小程序支付
     * @param dto
     * @return
     */
    @PostMapping("/payConfirmMini")
    public PayInfoVo payConfirm(@RequestBody PayTransationConfirmDTO dto){
        return payService.payConfirm(dto);
    }

    /**
     * 预下单
     * @param dto
     * @return
     */
    @PostMapping("/preOrder")
    public PooulResponse<PreOrderVo> preOrder(@RequestBody PreOrderDto dto){
        return payService.preOrder(dto);
    }

    /**
     * 下单回调通知
     * @param data
     * @param request
     * @return
     */
    @PostMapping("/paidNotify")
    public String paidNotify(@RequestBody String data, HttpServletRequest request){
        PooulResponse<PayNotifyResponse> response = payService.paidNotify(data);
        int code = response.getCode();
        if (0 == code) {
            request.getServletContext().setAttribute(response.getData().getMch_trade_id(),0);
            return "success";
        }
        return "fail";
    }

    /**
     * 支付结果
     * @param dto
     * @param request
     * @return
     */
    @PostMapping("/payResult")
    public Integer payResult(@RequestBody PayResultGetDto dto, HttpServletRequest request){
        String merchTradeId = dto.getMerchTradeId();
        ServletContext servletContext = request.getServletContext();
        Object result = servletContext.getAttribute(merchTradeId);
        if (Objects.nonNull(result)) {
            servletContext.removeAttribute(merchTradeId);
            return (Integer) result;
        }
        return 1;
    }
}
