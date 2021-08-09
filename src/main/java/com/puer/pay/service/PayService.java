package com.puer.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.puer.pay.dto.*;
import com.puer.pay.util.JsonUtils;
import com.puer.pay.util.MapperUtils;
import com.puer.pay.util.StringUtils;
import com.puer.pay.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.dozer.Mapping;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author shenggongjie
 * @date 2021/7/2 15:45
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayService {

    private final ApplicationContext applicationContext;

    public PayInfoVo payConfirm(PayTransationConfirmDTO dto) {
        String notifyUrl = applicationContext.getEnvironment().getProperty("wechat.notifyurl");
        String preorderUrl = applicationContext.getEnvironment().getProperty("pooul.pay.url");
        if (Strings.isBlank(preorderUrl)) {
            return new PayInfoVo();
        }
        String url = MessageFormat.format(preorderUrl, dto.getMerchId());
        //填充数据
        UUID uuid = UUID.randomUUID();
        PreOrderRequest preOrderRequest = new PreOrderRequest();
        preOrderRequest.setNotify_url(notifyUrl);
        preOrderRequest.setPay_type("wechat.jsminipg");
        preOrderRequest.setMch_trade_id(uuid.toString());
        preOrderRequest.setTotal_fee(dto.getPaidAmount().multiply(BigDecimal.valueOf(100)).intValue());
        preOrderRequest.setBody(uuid + "订单支付");
        preOrderRequest.setSub_appid("wx79f3783ab5cd5d05");
        preOrderRequest.setSub_openid("o6ZcR5XxdH6xFc1zxjwrvVjglZu8");
        preOrderRequest.setNonce_str(StringUtils.nonceStr(32));
        preOrderRequest.setUrl(url);
        //支付
        Map claimMap = callPay(preOrderRequest);
        //是否成功
        if (claimMap.get("data") instanceof Claim) {
            //转移数据
            PooulResponse<PreOrderResponse> response = dataTransfer(claimMap);
            if (response.getCode() == 0) {
                return MapperUtils.map(response.getData().getPayInfoVo(), PayInfoVo.class);
            }
        }
        ResponseVo responseVo = new ResponseVo();
        responseVo.setMessage(claimMap.get("msg").toString());
        responseVo.setCode((int) claimMap.get("code"));
        return responseVo;
    }

    public PooulResponse<PreOrderVo> preOrder(PreOrderDto dto) {
        String notifyUrl = applicationContext.getEnvironment().getProperty("wechat.notifyurl");
        String preorderUrl = applicationContext.getEnvironment().getProperty("pooul.pay.preOrder");
        if (Strings.isBlank(preorderUrl)) {
            return new PooulResponse<>();
        }
        String url = MessageFormat.format(preorderUrl, dto.getMerchId());
        //填充数据
        UUID uuid = UUID.randomUUID();
        String merchTradeId = uuid.toString();
        dto.setNotify_url(notifyUrl);
        dto.setMch_pre_id(merchTradeId);
        dto.setUrl(url);
        dto.setTotal_fee(dto.getMoney().multiply(BigDecimal.valueOf(100)).intValue());
        //支付
        Map claimMap = callPay(dto);
        if (claimMap.get("data") instanceof Claim) {
            //转移数据
            PooulResponse<PreOrderVo> response = preOrderTransfer(claimMap);
            PreOrderVo data = response.getData();
            if (Objects.nonNull(data)) {
                data.setMerchTradeId(merchTradeId);
            }
            log.info("交易单号是: {}", uuid);
            return response;
        }
        return MapperUtils.map(claimMap, PooulResponse.class);
    }


    public Map callPay(MerchUrlDto params) {
        String url = params.getUrl();
        //加密请求数据
        String data = JSON.toJSONString(params);
        RSAPrivateKey privateKey = getPrivateKey();
        String encryptionData = encryptionData(data, privateKey);
        try {
            HttpResponse response = Request.Post(url)
                    .body(new StringEntity(encryptionData, Charsets.UTF_8))
                    .connectTimeout(3000)
                    .execute()
                    .returnResponse();
            int status = response.getStatusLine().getStatusCode();
            String str = new String(EntityUtils.toByteArray(response.getEntity()));
            if (HttpStatus.SC_OK != status) {
                log.error("接口调用异常，状态码：{}", status);
            }
            if (null == response.getEntity()) {
                log.error("接口调用异常，未获取到返回信息");
            }
            try {
                return JSON.parseObject(str, Map.class);
            } catch (Exception e) {
                log.info("预下单成功");
            }
            //成功
            return parseJWT(str);
        } catch (Exception e) {
            log.error("接口调用异常：{}" + e.getMessage());
        }
        return null;
    }

    private String encryptionData(String data, RSAPrivateKey privateKey) {
        JWTCreator.Builder builder = JWT.create();
        Map<String, Object> map = JsonUtils.toMap(data);
        if (Objects.nonNull(map)) {
            map.forEach((key, value) -> builder.withClaim(key, value.toString()));
        }
        return builder.sign(Algorithm.RSA256(null, privateKey));
    }

    public static RSAPublicKey getPublicKeyFromString(String key) {
        Pattern parse = compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
        String encoded = parse.matcher(key).replaceFirst("$1").trim();
        byte[] decodeBase64 = Base64.decodeBase64(encoded);
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(decodeBase64));
        } catch (Exception e) {
            log.error("预下单接口调用异常: {}", e.getMessage());
        }
        return null;
    }

    public static RSAPrivateKey getPrivateKeyFromString(String key) {
        // 去掉文件头尾，只保留编码部分
        Pattern parse = compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
        String encoded = parse.matcher(key).replaceFirst("$1").trim();
        byte[] decodeBase64 = Base64.decodeBase64(encoded);
        RSAPrivateKey pKey = null;
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodeBase64);
            pKey = (RSAPrivateKey) kf.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return pKey;
    }


    /**
     * 解密jwt
     */
    public static Map<String, Claim> parseJWT(String jwt) {
        return JWT
                .require(Algorithm.RSA256(getPublicKey(), null))
                .build()
                .verify(jwt)
                .getClaims();
    }

    public static RSAPrivateKey getPrivateKey() {
        // 你需要修改为自己的证书位置
        java.nio.file.Path keyFile = Paths.get("src/main/resources/keys/rsa_private_pkcs8.pem");
        if (!Files.exists(keyFile)) {
            log.error("该商户私钥文件不存在");
        }

        // 从文件获取私钥
        String keyString = null;
        try {
            keyString = new String(Files.readAllBytes(keyFile), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getPrivateKeyFromString(keyString);
    }

    public static RSAPublicKey getPublicKey() {
        // 你需要修改为自己的证书位置
        java.nio.file.Path keyFile = Paths.get("src/main/resources/keys/puer_public.pem");
        if (!Files.exists(keyFile)) {
            log.error("该商户公钥文件不存在");
        }

        // 从文件获取私钥
        String keyString = null;
        try {
            keyString = new String(Files.readAllBytes(keyFile), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getPublicKeyFromString(keyString);
    }

    private PooulResponse<PreOrderVo> preOrderTransfer(Map<String, Claim> data) {
        PooulResponse<PreOrderVo> result = new PooulResponse<>();
        Integer code = data.get("code").asInt();
        String msg = data.get("msg").asString();
        Map<String, Object> dataMap = data.get("data").asMap();
        PreOrderVo response = MapperUtils.map(dataMap, PreOrderVo.class);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(response);
        return result;
    }

    private static PooulResponse<PreOrderResponse> dataTransfer(Map<String, Claim> data) {
        PooulResponse<PreOrderResponse> result = new PooulResponse<>();
        Integer code = data.get("code").asInt();
        String msg = data.get("msg").asString();
        Map<String, Object> dataMap = data.get("data").asMap();
        result.setCode(code);
        result.setMsg(msg);
        PreOrderResponse response = MapperUtils.map(dataMap, PreOrderResponse.class);
        String payInfo = response.getPay_info();
        PayInfoVo payInfoVo = JSON.parseObject(payInfo, PayInfoVo.class);
        Map<String, Object> payMap = JsonUtils.toMap(payInfo);
        if (Objects.nonNull(payMap)) {
            Object aPackage = payMap.get("package");
            if (Objects.nonNull(aPackage)) {
                String packageStr = aPackage.toString();
                payInfoVo.setPackageStr(packageStr);
                String[] fields = packageStr.split("=");
                if (fields.length > 0) {
                    payInfoVo.setPayPackage(fields[1]);
                }
            }
        }
        response.setPayInfoVo(payInfoVo);
        result.setData(response);
        return result;
    }

    /**
     * 回调
     *
     * @param data
     */
    public PooulResponse<PayNotifyResponse> paidNotify(String data) {
        return parseNotifyData(data, PayNotifyResponse.class);
    }

    public <T> PooulResponse<T> parseNotifyData(String data, Class<T> classz) {
        PooulResponse<T> result = new PooulResponse<>();
        try {
            result.setCode(ResponseCode.PRE_ORDER_FAIL.getCode());
            JSONObject json = JSON.parseObject(data);
            int code = json.getInteger("code");
            if (code == 0) {
                T content = json.getObject("data", classz);
                result.setCode(code).setData(content);
            }
            return result.setMsg(json.getString("msg"));
        } catch (Exception e) {
            try {
                JWTVerifier verifier = JWT.require(Algorithm.RSA256(getPublicKey(), null)).build();
                Map<String, Claim> claims = verifier.verify(data).getClaims();
                Integer integer = claims.get("code").asInt();
                Map<String, Object> map = claims.get("data").asMap();
                if (0 != integer) {
                    throw new RuntimeException("支付失败");
                }
                String mtId = map.get("mch_trade_id").toString();
                String originMerch = mtId.substring(0, mtId.length() - 2);
                map.put("mch_trade_id", originMerch);
                result.setCode(claims.get("code").asInt());
                result.setMsg(claims.get("msg").asString());
                result.setData(MapperUtils.map(map, classz));
                log.info("支付成功");
                log.info("交易单号是: {}", originMerch);
                return result;
            } catch (Exception ex) {
                return result.setCode(ResponseCode.PRE_ORDER_FAIL.getCode()).setMsg(ex.getMessage());
            }
        }
    }

    public PayLoginVo payLogin(PayLoginDto dto, HttpServletResponse httpServletResponse) {
        String loginUrl = applicationContext.getEnvironment().getProperty("pooul.pay.login");
        String requestStr = JSON.toJSONString(dto);
        try {
            HttpResponse response = Request.Post(loginUrl)
                    .body(new StringEntity(requestStr, Charsets.UTF_8))
                    .connectTimeout(3000)
                    .execute()
                    .returnResponse();
            String authorization = response.getFirstHeader("Authorization").getValue();
            int status = response.getStatusLine().getStatusCode();
            String str = new String(EntityUtils.toByteArray(response.getEntity()));
            if (HttpStatus.SC_OK != status) {
                log.error("接口调用异常，状态码：{}", status);
            }
            if (null == response.getEntity()) {
                log.error("接口调用异常，未获取到返回信息");
            }
            Map map = JSON.parseObject(str, Map.class);
            httpServletResponse.setHeader("Authorization",authorization);
            return MapperUtils.map(map, PayLoginVo.class);
        } catch (IOException e) {
            log.error("请求登录失败: {}", e.getMessage(), e);
        }
        return null;
    }

    public PayCreateChildVo payCreateChild(PayCreateChildDto dto) {
        String createUrl = applicationContext.getEnvironment().getProperty("pooul.pay.create");
        try {
            String requestStr = JSON.toJSONString(dto);
            HttpResponse response = Request.Post(createUrl)
                    .addHeader("Authorization", dto.getAuthorization())
                    .body(new StringEntity(requestStr, Charsets.UTF_8))
                    .connectTimeout(3000)
                    .execute()
                    .returnResponse();
            int status = response.getStatusLine().getStatusCode();
            String str = new String(EntityUtils.toByteArray(response.getEntity()));
            if (HttpStatus.SC_OK != status) {
                log.error("接口调用异常，状态码：{}", status);
            }
            if (null == response.getEntity()) {
                log.error("接口调用异常，未获取到返回信息");
            }
            Map map = JSON.parseObject(str, Map.class);
            return MapperUtils.map(map, PayCreateChildVo.class);
        } catch (IOException e) {
            log.error("请求创建入驻商户失败: {}", e.getMessage(), e);
        }
        return null;
    }
}
