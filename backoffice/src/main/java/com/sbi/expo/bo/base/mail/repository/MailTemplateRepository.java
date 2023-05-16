package com.sbi.expo.bo.base.mail.repository;

import com.sbi.expo.bo.base.mail.model.MailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MailTemplateRepository
        extends JpaRepository<MailTemplate, Long>, JpaSpecificationExecutor<MailTemplate> {
    /**
     * query by Keyword
     *
     * @param keyword /
     * @return /
     */
    MailTemplate findByKeyword(String keyword);
}
