package com.sbi.dl.compoment.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbi.dl.compoment.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.sbi.dl.compoment.constant.CommonConstant.COMMA;
import static com.sbi.dl.compoment.constant.CommonConstant.SINGLE_QUOTES;

/** JpaUtils */
@Slf4j
public class BitsSqlUtil {

    private static final EntityManager em;
    private static final ObjectMapper om;

    private static final String INSERT_PREFIX = "INSERT INTO ";
    private static final String DELETE_PREFIX = "DELETE FROM  ";
    private static final String DELETE_SUFFIX = " WHERE id in (:ids)";
    private static final String INSERT_VALUES = " VALUES";
    private static final String INSERT_LEFT_PARENTHESIS = " (";
    private static final String INSERT_RIGHT_PARENTHESIS = ") ";
    private static final int BATCH_NUMBER = 1000;

    static {
        em = SpringContextHolder.getBean(EntityManager.class);
        om = SpringContextHolder.getBean(ObjectMapper.class);
    }

    //    public static <T> int insertAll(List<T> list, Class<T> entityType) {
    //        // TODO TBD
    //        return 1;
    //    }
    //
    //    public static <T extends Model> int insertAll(List<T> list) {
    //        // TODO TBD
    //        return 1;
    //    }

    public static int insertAll(List<?> list, String tableName) {
        if (CollectionUtils.isEmpty(list) || StringUtils.isEmpty(tableName)) {
            return 0;
        }
        // convert object to map & convert CamelCase to Underline
        List<Map<String, Object>> mapList =
                list.stream()
                        .map(
                                obj -> {
                                    Map<String, Object> m = new LinkedHashMap<>(list.size() * 2);
                                    om.convertValue(obj, Map.class)
                                            .forEach(
                                                    (k, v) ->
                                                            m.put(toUnderlineCase((String) k), v));
                                    return m;
                                })
                        .collect(Collectors.toList());
        // assemble sql
        StringBuilder sb =
                new StringBuilder(INSERT_PREFIX).append(tableName).append(INSERT_LEFT_PARENTHESIS);
        mapList.get(0).forEach((k, v) -> sb.append(k).append(COMMA));
        // preSql: insert into table (xx,xx) values
        String preSql =
                sb.deleteCharAt(sb.lastIndexOf(COMMA))
                        .append(INSERT_RIGHT_PARENTHESIS)
                        .append(INSERT_VALUES)
                        .toString();

        List<String> sqlList = getSql(mapList, preSql);
        AtomicInteger row = new AtomicInteger();
        sqlList.forEach(
                sql -> {
                    Query insert = em.createNativeQuery(sql);
                    row.addAndGet(insert.executeUpdate());
                });
        log.info(
                "Insert all successfully, table:{}, batch:{}, row:{}",
                tableName,
                sqlList.size(),
                row);
        return row.get();
    }

    public static void deleteAllByIds(List<Long> ids, String tableName) {
        log.info("delete : {}, from table :{}", ids, tableName);
        if (CollectionUtils.isEmpty(ids) || StringUtils.isEmpty(tableName)) {
            throw new RuntimeException();
        }
        StringBuilder sb = new StringBuilder(DELETE_PREFIX).append(tableName).append(DELETE_SUFFIX);
        Query query = em.createNativeQuery(sb.toString());
        query.setParameter("ids", ids);
        query.executeUpdate();
    }

    private static List<String> getSql(List<Map<String, Object>> mapList, String preSql) {
        List<String> sqlList = new ArrayList<>();
        if (mapList.size() > BATCH_NUMBER) {
            ListUtils.partition(mapList, BATCH_NUMBER)
                    .forEach(batchList -> sqlList.add(appendValues(batchList, preSql)));
        } else {
            sqlList.add(appendValues(mapList, preSql));
        }
        return sqlList;
    }

    private static String appendValues(List<Map<String, Object>> mapList, String preSql) {
        StringBuilder valuesSql = new StringBuilder(preSql);
        mapList.forEach(
                map -> {
                    valuesSql.append(INSERT_LEFT_PARENTHESIS);

                    map.forEach(
                            (k, v) ->
                                    valuesSql
                                            .append(SINGLE_QUOTES)
                                            .append(v)
                                            .append(SINGLE_QUOTES)
                                            .append(COMMA));

                    valuesSql
                            .deleteCharAt(valuesSql.lastIndexOf(COMMA))
                            .append(INSERT_RIGHT_PARENTHESIS)
                            .append(COMMA);
                });
        return valuesSql.deleteCharAt(valuesSql.lastIndexOf(COMMA)).toString();
    }

    /**
     * convert CamelCase to Underline
     *
     * @param camelCaseStr
     * @return userName -> user_name
     */
    private static String toUnderlineCase(String camelCaseStr) {
        if (camelCaseStr == null) {
            return null;
        }
        char[] charArray = camelCaseStr.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0, l = charArray.length; i < l; i++) {
            if (charArray[i] >= 65 && charArray[i] <= 90) {
                sb.append("_").append(charArray[i] += 32);
            } else {
                sb.append(charArray[i]);
            }
        }
        return sb.toString();
    }
}
