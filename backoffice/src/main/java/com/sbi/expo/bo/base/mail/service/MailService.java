package com.sbi.expo.bo.base.mail.service;

public interface MailService {

    /**
     * !!!WARNING: this mail module is committed without testing over a real environment, since we
     * don't have one for now. Please verify carefully. send email
     *
     * @param receiveAddress receiver address
     * @param keyword mail keyword
     * @param args args
     */
    void sendByTemplateKeyword(String receiveAddress, String keyword, String... args);
}
