package com.sbi.expo.bo.base.mail.dao;

import com.sbi.expo.commons.annotation.Query;
import lombok.Data;

@Data
public class MailTemplateQueryCriteria {

    /** equal */
    @Query private Long id;

    /** blur */
    @Query(type = Query.Type.INNER_LIKE)
    private String keyword;

    /** equal */
    @Query private String parameters;

    /** blur */
    @Query(type = Query.Type.INNER_LIKE)
    private String description;

    /** blur */
    @Query(type = Query.Type.INNER_LIKE)
    private String subject;

    /** blur */
    @Query(type = Query.Type.INNER_LIKE)
    private String contents;
}
