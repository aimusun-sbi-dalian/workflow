package com.sbi.expo.bo.base.mail.service;

import com.sbi.expo.bo.base.mail.dao.MailTemplateQueryCriteria;
import com.sbi.expo.bo.base.mail.dto.MailTemplateDTO;
import com.sbi.expo.bo.base.mail.form.MailTemplateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MailTemplateService {

    /**
     * get data with pages
     *
     * @param criteria condition
     * @param pageable page params
     * @return Map<String,Object>
     */
    Page<MailTemplateDTO> queryAll(MailTemplateQueryCriteria criteria, Pageable pageable);

    /**
     * query by Id
     *
     * @param id ID
     * @return MailTemplateDTO
     */
    MailTemplateDTO findById(Long id);

    /**
     * create
     *
     * @param resources /
     * @return MailTemplateDTO
     */
    MailTemplateDTO create(MailTemplateForm resources);

    /**
     * edit
     *
     * @param resources /
     */
    void update(Long templateId, MailTemplateForm resources);

    /**
     * remove by Id
     *
     * @param id /
     */
    void deleteById(Long id);

    /**
     * get mail templates
     *
     * @param form
     * @param pageable
     */
    Page<MailTemplateDTO> queryMailTemplateByPage(MailTemplateForm form, Pageable pageable);
}
