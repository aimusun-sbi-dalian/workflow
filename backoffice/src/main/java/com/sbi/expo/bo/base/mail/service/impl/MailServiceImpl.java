package com.sbi.expo.bo.base.mail.service.impl;

import com.sbi.expo.bo.base.constant.MessageConstant;
import com.sbi.expo.bo.base.exceptions.BizPromptException;
import com.sbi.expo.bo.base.mail.dto.MailTemplateDTO;
import com.sbi.expo.bo.base.mail.mapper.MailTemplateMapper;
import com.sbi.expo.bo.base.mail.repository.MailTemplateRepository;
import com.sbi.expo.bo.base.mail.service.MailService;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Value("${mail.enable:true}")
    Boolean enableMail;

    @Value("${mail.from.address}")
    String fromAddress;

    @Value("${mail.from.person}")
    String fromPerson;

    @Autowired private JavaMailSender javaMailSender;

    @Autowired private MailTemplateMapper mailTemplateMapper;

    @Autowired private MailTemplateRepository mailTemplateRepository;

    @Override
    public void sendByTemplateKeyword(String receiveAddress, String keyword, String... args) {
        MailTemplateDTO dto =
                mailTemplateMapper.toDTO(mailTemplateRepository.findByKeyword(keyword));
        if (Objects.isNull(dto)) {
            throw new BizPromptException(MessageConstant.CODE_10401);
        }
        try {
            send(new InternetAddress(receiveAddress), dto, args);
            log.info(
                    "Send mail successfully, [receiveAddress={}, keyword={},args={}]",
                    receiveAddress,
                    keyword,
                    args);
        } catch (Exception e) {
            log.error(
                    "Send mail failed, [receiveAddress={}, keyword={},args={}], exception stack"
                            + " info: {}",
                    receiveAddress,
                    keyword,
                    args,
                    e.getMessage());
        }
    }

    private void send(InternetAddress to, MailTemplateDTO template, String... args)
            throws MessagingException, IOException {
        if (BooleanUtils.isTrue(enableMail)) {
            javaMailSender.send(createMimeMessage(to, template, args));
        } else {
            log.info(
                    "[Mail disabled] Just skip the mail {} for {}",
                    template.getSubject(),
                    to.getAddress());
        }
    }

    private MimeMessage createMimeMessage(
            InternetAddress to, MailTemplateDTO template, String... args)
            throws IOException, MessagingException {
        log.info(
                "Mail template's subject:{} , content:{}",
                template.getSubject(),
                template.getContent());
        MessageFormat format = new MessageFormat(template.getContent());
        MimeMessage message = javaMailSender.createMimeMessage();

        InternetAddress fromInternetAddress =
                new InternetAddress(this.fromAddress, this.fromPerson);
        message.setRecipient(Message.RecipientType.TO, to);
        message.setFrom(fromInternetAddress);
        message.setSubject(template.getSubject(), template.getCharset());
        message.setText(format.format(args), template.getCharset());
        log.info(
                "MimeMessage's subject:{} , content:{}",
                message.getSubject(),
                message.getContent());
        return message;
    }
}
