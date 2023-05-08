package com.sbi.dl.compoment.utils;

import org.springframework.data.domain.Page;

import java.util.LinkedHashMap;
import java.util.Map;

public class BitsPageUtil extends cn.hutool.core.util.PageUtil {

    /** Page process in case of error from redis */
    public static Map<String, Object> toPage(Page page) {
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", page.getContent());
        map.put("totalElements", page.getTotalElements());
        return map;
    }

    /** customize page */
    public static Map<String, Object> toPage(Object object, Object totalElements) {
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", object);
        map.put("totalElements", totalElements);
        return map;
    }
}
