package com.sbi.expo.bo.base.mail.form;

import lombok.Data;

@Data
public class MailTemplateForm {

    private Long id;

    private String keyword;

    private String parameters;

    private String description;

    private String subject;

    private String content;
}
