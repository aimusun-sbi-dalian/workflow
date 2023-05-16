package com.sbi.expo.bo.base.mail.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class MailTemplateDTO implements Serializable {

    private Long id;

    private String keyword;

    private String parameters;

    private String description;

    private String subject;

    private String charset;

    private String content;
}
