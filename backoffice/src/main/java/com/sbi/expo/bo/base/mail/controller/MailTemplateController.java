package com.sbi.expo.bo.base.mail.controller;

import com.sbi.expo.bo.base.ResponseBase;
import com.sbi.expo.bo.base.mail.dao.MailTemplateQueryCriteria;
import com.sbi.expo.bo.base.mail.dto.MailTemplateDTO;
import com.sbi.expo.bo.base.mail.form.MailTemplateForm;
import com.sbi.expo.bo.base.mail.service.MailTemplateService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/int/v1/mail/template")
public class MailTemplateController {

    @Autowired private MailTemplateService mailTemplateService;

    @GetMapping("/{id}")
    @PreAuthorize("@auth.check('sys:mailTemplate:read')")
    public ResponseBase<MailTemplateDTO> queryMailTemplate(@PathVariable("id") Long templateId) {
        return ResponseBase.ok(mailTemplateService.findById(templateId));
    }

    @GetMapping
    @PreAuthorize("@auth.check('sys:mailTemplate:read')")
    public ResponseBase<Page<MailTemplateDTO>> queryMailTemplate(
            MailTemplateQueryCriteria criteria, Pageable pageable) {
        return ResponseBase.ok(mailTemplateService.queryAll(criteria, pageable));
    }

    @PostMapping
    @PreAuthorize("@auth.check('sys:mailTemplate:create')")
    public ResponseBase<MailTemplateDTO> createMailTemplate(
            @Validated @RequestBody MailTemplateForm resources) {
        return ResponseBase.ok(mailTemplateService.create(resources));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@auth.check('sys:mailTemplate:modify')")
    public ResponseBase<String> updateMailTemplate(
            @PathVariable("id") Long templateId, @Valid @RequestBody MailTemplateForm resources) {
        mailTemplateService.update(templateId, resources);
        return ResponseBase.ok();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@auth.check('sys:mailTemplate:delete')")
    public ResponseBase<String> deleteMailTemplate(@PathVariable("id") Long templateId) {
        mailTemplateService.deleteById(templateId);
        return ResponseBase.ok();
    }
}
