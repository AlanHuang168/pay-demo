package com.puer.pay.util;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author shenggongjie
 * @date 2021/4/27 9:31
 */
public class MapperUtils {

    private static final Mapper MAPPER = new DozerBeanMapper();

    public static <T> T map(Object source,Class<T> destinationClass){
        return source == null ? null : MAPPER.map(source, destinationClass);
    }

    public static <T> List<T> mapList(List<?> source,Class<T> destinationClass){
        if (source == null) {
            return null;
        }
        List<T> result = new ArrayList<>();
        for (Object object : source) {
            T destination = map(object, destinationClass);
            result.add(destination);
        }
        return result;
    }

    public static <T> List<T> mapList(List<?> source, Class<T> destinationClass, Function<T,T> addField){
        if (source == null) {
            return null;
        }
        List<T> result = new ArrayList<>();
        for (Object object : source) {
            T destination = map(object, destinationClass);
            addField.apply(destination);
            result.add(destination);
        }
        return result;
    }
}
