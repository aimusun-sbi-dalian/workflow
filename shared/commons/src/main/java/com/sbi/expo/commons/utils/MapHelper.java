package com.sbi.expo.commons.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * MapUtils
 *
 * @author Ming.G
 * @date 2021-01-26
 */
public class MapHelper {

    private MapHelper() {}

    /**
     * create and init map, odd-numbered parameters as key and even-numbered parameters as value
     * <br>
     * E.q : MapUtils.initMap("k1","v1","k2","v2"); => {k1=v1, k2=v2}
     *
     * @author Ming.G
     * @date 2021-01-27
     */
    public static Map<String, Object> initMap(Object... params) {
        if (null == params || params.length == 0) {
            return new HashMap<>(0);
        }
        Map<String, Object> map = new HashMap<>(params.length / 2);
        int size = (params.length % 2 == 0 ? params.length : params.length - 1);
        for (int i = 0; i < size; ) {
            map.put(String.valueOf(params[i++]), params[i++]);
        }
        return map;
    }

    /**
     * create and init map, odd-numbered parameters as key and even-numbered parameters as value
     * <br>
     * E.q : MapUtils.initMap("k1","v1","k2","v2"); => {k1=v1, k2=v2}
     *
     * @author Ming.G
     * @date 2021-02-02
     */
    public static Map<String, String> initMapForString(String... params) {
        if (null == params || params.length == 0) {
            return new HashMap<>(0);
        }
        Map<String, String> map = new HashMap<>(params.length / 2);
        int size = (params.length % 2 == 0 ? params.length : params.length - 1);
        for (int i = 0; i < size; ) {
            map.put(params[i++], params[i++]);
        }
        return map;
    }

    /**
     * check map is empty
     *
     * @param map target map
     * @return true if map is empty
     * @author Ming.G
     * @date 2021-5-24 10:08:04
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    /**
     * check map is not empty
     *
     * @param map target map
     * @return true if map is not empty
     * @author Ming.G
     * @date 2021-5-24 10:08:04
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }
}
