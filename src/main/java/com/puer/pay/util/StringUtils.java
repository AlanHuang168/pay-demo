package com.puer.pay.util;

import org.springframework.util.DigestUtils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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

    public static String nonceStr(int len){
        String str = "";
        String strPol = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
        int max = strPol.length();
        Random random = new Random();
        for(int i=0; i < len; i++){
            str += strPol.charAt(random.nextInt(max));
        }
        return str;
    }

}
