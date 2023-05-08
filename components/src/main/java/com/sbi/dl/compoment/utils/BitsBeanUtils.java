package com.sbi.dl.compoment.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class BitsBeanUtils {

    public static List<String> differentFields(
            Object source, Object target, List<String> ignoreFields) {
        Field[] fields = FieldUtils.getAllFields(source.getClass());
        return Stream.of(fields)
                .filter(
                        field -> {
                            if (ignoreFields.contains(field.getName())) {
                                return false;
                            }
                            try {
                                field.setAccessible(true);
                                return ObjectUtils.notEqual(field.get(source), field.get(target));
                            } catch (IllegalAccessException e) {
                                return false;
                            }
                        })
                .map(Field::getName)
                .toList();
    }

    public static void deepCopy(Object source, Object target) {
        try {
            org.apache.commons.beanutils.BeanUtils.copyProperties(target, source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.warn("deep copy error", e);
            throw new RuntimeException();
        }
    }

    public static void shallowCopy(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

    public static void shallowCopy(Object source, Object target, String... ignoreProperties) {
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }
}
