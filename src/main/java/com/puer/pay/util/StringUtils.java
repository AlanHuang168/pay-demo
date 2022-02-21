package com.puer.pay.util;

import com.alibaba.fastjson.JSON;
import com.puer.pay.vo.RoyaltiesVo;
import org.springframework.util.DigestUtils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @Description:
 * @Author: Alan
 * @date: 2021-06-11 10:42
 */
public class StringUtils {

    public static String generateSerialNumber(String prefix) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(3);
        String code = nf.format(Math.random() * 10).replace(".", "");
        String time = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date());
        if(null != prefix) {
            return prefix + time + code;
        }
        return time + code;
    }

    public static String shortUUID(){
        return DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes()).substring(8, 23);
    }

    public static String UUID() {
        return DigestUtils.md5DigestAsHex(UUID.randomUUID().toString().getBytes());
    }

    public static String nonceStr(int len) {
        StringBuilder str = new StringBuilder();
        String strPol = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        int max = strPol.length();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            str.append(strPol.charAt(random.nextInt(max)));
        }
        return str.toString();
    }

    public static String[] transToArr(String data) {
        return getJsonList(data);
    }

    public static RoyaltiesVo[] transToRoyaltiesArr(String data) {
        String[] fields = getJsonList(data);
        RoyaltiesVo[] royaltiesArr = new RoyaltiesVo[fields.length];
        for (int i = 0; i < fields.length; i++) {
            royaltiesArr[i] = JSON.parseObject(fields[i], RoyaltiesVo.class);
        }
        return royaltiesArr;
    }

    private static String[] getJsonList(String data) {
        String[] fields = data.replaceAll("\\[", "")
                .replaceAll("]", "")
                .replaceAll(" {3}", "")
                .replaceAll(" {2}", "")
                .replaceAll(" ", "")
                .replaceAll("=", ":")
                .split("},");
        if (fields.length > 1) {
            for (int i = 0; i < fields.length; i++) {
                if (i < fields.length - 1) {
                    fields[i] = fields[i] + "}";
                }
            }
        }
        return fields;
    }
}
