package com.sbi.dl.compoment.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Zhenming.Zhang
 * @date 2022/06/15
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    // attribute name of base object
    String propName() default "";
    // query method
    Type type() default Type.EQUAL;

    /** get joined attribute name */
    String joinName() default "";

    /** default left join */
    Join join() default Join.LEFT;

    /**
     * multi field fuzzy search, only support String which is separated by ',' Example @Query(blurry
     * = "email, username")
     */
    String blurry() default "";

    enum Type {
        EQUAL,
        GREATER_THAN,
        LESS_THAN,
        INNER_LIKE,
        LEFT_LIKE,
        RIGHT_LIKE,
        LESS_THAN_NQ,
        IN,
        NOT_IN,
        NOT_EQUAL,
        BETWEEN,
        NOT_NULL,
        IS_NULL
    }

    /** for simple join query */
    enum Join {
        LEFT,
        RIGHT,
        INNER
    }
}
