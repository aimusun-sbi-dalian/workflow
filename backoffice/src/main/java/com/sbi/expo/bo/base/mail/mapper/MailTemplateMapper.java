package com.sbi.expo.bo.base.mail.mapper;

import com.sbi.expo.bo.base.mail.dto.MailTemplateDTO;
import com.sbi.expo.bo.base.mail.model.MailTemplate;
import com.sbi.expo.commons.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MailTemplateMapper extends BaseMapper<MailTemplateDTO, MailTemplate> {}
