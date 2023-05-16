package com.sbi.expo.bo.base.mail.service.impl;

import com.sbi.expo.bo.base.constant.MessageConstant;
import com.sbi.expo.bo.base.exceptions.BizPromptException;
import com.sbi.expo.bo.base.mail.dao.MailTemplateQueryCriteria;
import com.sbi.expo.bo.base.mail.dto.MailTemplateDTO;
import com.sbi.expo.bo.base.mail.form.MailTemplateForm;
import com.sbi.expo.bo.base.mail.mapper.MailTemplateMapper;
import com.sbi.expo.bo.base.mail.model.MailTemplate;
import com.sbi.expo.bo.base.mail.repository.MailTemplateRepository;
import com.sbi.expo.bo.base.mail.service.MailTemplateService;
import com.sbi.expo.commons.CommonConstant;
import com.sbi.expo.commons.utils.QueryHelper;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class MailTemplateServiceImpl implements MailTemplateService {

    @Autowired private MailTemplateRepository mailTemplateRepository;
    @Autowired private MailTemplateMapper mailTemplateMapper;

    @Override
    public MailTemplateDTO findById(Long id) {
        MailTemplate mailTemplate =
                mailTemplateRepository
                        .findById(id)
                        .orElseThrow(() -> new BizPromptException(MessageConstant.CODE_10401));
        return mailTemplateMapper.toDTO(mailTemplate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MailTemplateDTO create(MailTemplateForm resources) {
        if (ObjectUtils.isNotEmpty(mailTemplateRepository.findByKeyword(resources.getKeyword()))) {
            throw new BizPromptException(MessageConstant.CODE_10402);
        }
        MailTemplate mailTemplate =
                MailTemplate.builder()
                        .keyword(resources.getKeyword())
                        .parameters(resources.getParameters())
                        .description(resources.getDescription())
                        .subject(resources.getSubject())
                        .content(resources.getContent())
                        .charset(StandardCharsets.UTF_8.name())
                        .build();
        return mailTemplateMapper.toDTO(mailTemplateRepository.save(mailTemplate));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long templateId, MailTemplateForm resources) {
        MailTemplate mailTemplate =
                mailTemplateRepository
                        .findById(templateId)
                        .orElseThrow(() -> new BizPromptException(MessageConstant.CODE_10401));
        BeanUtils.copyProperties(
                resources,
                mailTemplate,
                "id",
                "keyword",
                "charset",
                "deleted",
                "createdAt",
                "updatedAt",
                "version");
        mailTemplateRepository.save(mailTemplate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        mailTemplateRepository.deleteById(id);
    }

    @Override
    public Page<MailTemplateDTO> queryMailTemplateByPage(MailTemplateForm form, Pageable pageable) {
        Specification<MailTemplate> spec =
                Specification.where(
                        (root, query, cb) -> cb.equal(root.get(CommonConstant.DELETED), false));
        Page<MailTemplate> templatePage =
                mailTemplateRepository.findAll(
                        spec,
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by(Sort.Direction.DESC, CommonConstant.ID)));
        List<MailTemplate> templates = templatePage.getContent();
        List<MailTemplateDTO> result = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(templates)) {
            result = mailTemplateMapper.toDTO(templates);
        }
        return new PageImpl<>(result, pageable, templatePage.getTotalElements());
    }

    @Override
    public Page<MailTemplateDTO> queryAll(MailTemplateQueryCriteria criteria, Pageable pageable) {
        Page<MailTemplate> page =
                mailTemplateRepository.findAll(
                        (root, criteriaQuery, criteriaBuilder) ->
                                QueryHelper.getPredicate(root, criteria, criteriaBuilder),
                        pageable);
        return page.map(mailTemplateMapper::toDTO);
    }
}
