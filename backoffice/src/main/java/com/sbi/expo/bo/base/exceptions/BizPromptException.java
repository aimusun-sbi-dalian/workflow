package com.sbi.expo.bo.base.exceptions;

import lombok.Getter;

/**
 * BizPromptException
 *
 * @author Ming.G
 * @date 2022-05-24
 */
@Getter
public class BizPromptException extends RuntimeException {
    private final String messageCode;
    private String message;

    public BizPromptException(String messageCode, String message) {
        super(message);
        this.messageCode = messageCode;
        this.message = message;
    }

    public BizPromptException(String messageCode) {
        super();
        this.messageCode = messageCode;
    }
}
