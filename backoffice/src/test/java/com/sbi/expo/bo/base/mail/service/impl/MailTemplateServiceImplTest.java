package com.sbi.expo.bo.base.mail.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sbi.expo.bo.base.exceptions.BizPromptException;
import com.sbi.expo.bo.base.mail.dao.MailTemplateQueryCriteria;
import com.sbi.expo.bo.base.mail.dto.MailTemplateDTO;
import com.sbi.expo.bo.base.mail.form.MailTemplateForm;
import com.sbi.expo.bo.base.mail.mapper.MailTemplateMapper;
import com.sbi.expo.bo.base.mail.model.MailTemplate;
import com.sbi.expo.bo.base.mail.repository.MailTemplateRepository;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

@ContextConfiguration(classes = {MailTemplateServiceImpl.class})
@ExtendWith(SpringExtension.class)
class MailTemplateServiceImplTest {
    @MockBean
    private MailTemplateMapper mailTemplateMapper;

    @MockBean
    private MailTemplateRepository mailTemplateRepository;

    @Autowired
    private MailTemplateServiceImpl mailTemplateServiceImpl;
    @Mock
    private EntityManager em;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private CriteriaQuery<MailTemplate> cq;
    @BeforeEach
    public void setUp() {
        em = Mockito.mock(EntityManager.class);
        cb = Mockito.mock(CriteriaBuilder.class);
        cq = Mockito.mock(CriteriaQuery.class);
    }

    /**
     * Method under test: {@link MailTemplateServiceImpl#findById(Long)}
     */
    @Test
    void testFindById() {
        MailTemplateDTO mailTemplateDTO = new MailTemplateDTO();
        mailTemplateDTO.setCharset("UTF-8");
        mailTemplateDTO.setContent("Not all who wander are lost");
        mailTemplateDTO.setDescription("The characteristics of someone or something");
        mailTemplateDTO.setId(1L);
        mailTemplateDTO.setKeyword("Keyword");
        mailTemplateDTO.setParameters("Parameters");
        mailTemplateDTO.setSubject("Hello from the Dreaming Spires");
        when(mailTemplateMapper.toDTO(Mockito.<MailTemplate>any())).thenReturn(mailTemplateDTO);

        MailTemplate mailTemplate = new MailTemplate();
        mailTemplate.preUpdate();
        mailTemplate.setCharset("UTF-8");
        mailTemplate.setContent("Not all who wander are lost");
        mailTemplate.setDescription("The characteristics of someone or something");
        mailTemplate.setId(1L);
        mailTemplate.setKeyword("Keyword");
        mailTemplate.setParameters("Parameters");
        mailTemplate.setSubject("Hello from the Dreaming Spires");
        mailTemplate.setVersion(1);
        Optional<MailTemplate> ofResult = Optional.of(mailTemplate);
        when(mailTemplateRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertSame(mailTemplateDTO, mailTemplateServiceImpl.findById(1L));
        verify(mailTemplateMapper).toDTO(Mockito.<MailTemplate>any());
        verify(mailTemplateRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link MailTemplateServiceImpl#findById(Long)}
     */
    @Test
    void testFindById2() {
        when(mailTemplateMapper.toDTO(Mockito.<MailTemplate>any()))
                .thenThrow(new BizPromptException("An error occurred"));

        MailTemplate mailTemplate = new MailTemplate();
        mailTemplate.preUpdate();
        mailTemplate.setCharset("UTF-8");
        mailTemplate.setContent("Not all who wander are lost");
        mailTemplate.setDescription("The characteristics of someone or something");
        mailTemplate.setId(1L);
        mailTemplate.setKeyword("Keyword");
        mailTemplate.setParameters("Parameters");
        mailTemplate.setSubject("Hello from the Dreaming Spires");
        mailTemplate.setVersion(1);
        Optional<MailTemplate> ofResult = Optional.of(mailTemplate);
        when(mailTemplateRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(BizPromptException.class, () -> mailTemplateServiceImpl.findById(1L));
        verify(mailTemplateMapper).toDTO(Mockito.<MailTemplate>any());
        verify(mailTemplateRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link MailTemplateServiceImpl#findById(Long)}
     */
    @Test
    void testFindById3() {
        MailTemplateDTO mailTemplateDTO = new MailTemplateDTO();
        mailTemplateDTO.setCharset("UTF-8");
        mailTemplateDTO.setContent("Not all who wander are lost");
        mailTemplateDTO.setDescription("The characteristics of someone or something");
        mailTemplateDTO.setId(1L);
        mailTemplateDTO.setKeyword("Keyword");
        mailTemplateDTO.setParameters("Parameters");
        mailTemplateDTO.setSubject("Hello from the Dreaming Spires");
        when(mailTemplateMapper.toDTO(Mockito.<MailTemplate>any())).thenReturn(mailTemplateDTO);
        when(mailTemplateRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        assertThrows(BizPromptException.class, () -> mailTemplateServiceImpl.findById(1L));
        verify(mailTemplateRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link MailTemplateServiceImpl#create(MailTemplateForm)}
     */
    @Test
    void testCreate() {
        MailTemplateDTO mailTemplateDTO = new MailTemplateDTO();
        mailTemplateDTO.setCharset("UTF-8");
        mailTemplateDTO.setContent("Not all who wander are lost");
        mailTemplateDTO.setDescription("The characteristics of someone or something");
        mailTemplateDTO.setId(1L);
        mailTemplateDTO.setKeyword("Keyword");
        mailTemplateDTO.setParameters("Parameters");
        mailTemplateDTO.setSubject("Hello from the Dreaming Spires");
        when(mailTemplateMapper.toDTO(Mockito.<MailTemplate>any())).thenReturn(mailTemplateDTO);

        MailTemplate mailTemplate = new MailTemplate();
        mailTemplate.preUpdate();
        mailTemplate.setCharset("UTF-8");
        mailTemplate.setContent("Not all who wander are lost");
        mailTemplate.setDescription("The characteristics of someone or something");
        mailTemplate.setId(1L);
        mailTemplate.setKeyword("Keyword");
        mailTemplate.setParameters("Parameters");
        mailTemplate.setSubject("Hello from the Dreaming Spires");
        mailTemplate.setVersion(1);

        MailTemplate mailTemplate2 = new MailTemplate();
        mailTemplate2.preUpdate();
        mailTemplate2.setCharset("UTF-8");
        mailTemplate2.setContent("Not all who wander are lost");
        mailTemplate2.setDescription("The characteristics of someone or something");
        mailTemplate2.setId(1L);
        mailTemplate2.setKeyword("Keyword");
        mailTemplate2.setParameters("Parameters");
        mailTemplate2.setSubject("Hello from the Dreaming Spires");
        mailTemplate2.setVersion(1);
        when(mailTemplateRepository.findByKeyword(Mockito.<String>any())).thenReturn(mailTemplate);
        when(mailTemplateRepository.save(Mockito.<MailTemplate>any())).thenReturn(mailTemplate2);

        MailTemplateForm resources = new MailTemplateForm();
        resources.setContent("Not all who wander are lost");
        resources.setDescription("The characteristics of someone or something");
        resources.setId(1L);
        resources.setKeyword("Keyword");
        resources.setParameters("Parameters");
        resources.setSubject("Hello from the Dreaming Spires");
        assertThrows(BizPromptException.class, () -> mailTemplateServiceImpl.create(resources));
        verify(mailTemplateRepository).findByKeyword(Mockito.<String>any());
    }

    /**
     * Method under test: {@link MailTemplateServiceImpl#create(MailTemplateForm)}
     */
    @Test
    void testCreate2() {
        MailTemplateDTO mailTemplateDTO = new MailTemplateDTO();
        mailTemplateDTO.setCharset("UTF-8");
        mailTemplateDTO.setContent("Not all who wander are lost");
        mailTemplateDTO.setDescription("The characteristics of someone or something");
        mailTemplateDTO.setId(1L);
        mailTemplateDTO.setKeyword("Keyword");
        mailTemplateDTO.setParameters("Parameters");
        mailTemplateDTO.setSubject("Hello from the Dreaming Spires");
        when(mailTemplateMapper.toDTO(Mockito.<MailTemplate>any())).thenReturn(mailTemplateDTO);
        when(mailTemplateRepository.findByKeyword(Mockito.<String>any())).thenThrow(new IllegalArgumentException("foo"));
        when(mailTemplateRepository.save(Mockito.<MailTemplate>any())).thenThrow(new IllegalArgumentException("foo"));

        MailTemplateForm resources = new MailTemplateForm();
        resources.setContent("Not all who wander are lost");
        resources.setDescription("The characteristics of someone or something");
        resources.setId(1L);
        resources.setKeyword("Keyword");
        resources.setParameters("Parameters");
        resources.setSubject("Hello from the Dreaming Spires");
        assertThrows(IllegalArgumentException.class, () -> mailTemplateServiceImpl.create(resources));
        verify(mailTemplateRepository).findByKeyword(Mockito.<String>any());
    }

    /**
     * Method under test: {@link MailTemplateServiceImpl#update(Long, MailTemplateForm)}
     */
    @Test
    void testUpdate() {
        MailTemplate mailTemplate = new MailTemplate();
        mailTemplate.preUpdate();
        mailTemplate.setCharset("UTF-8");
        mailTemplate.setContent("Not all who wander are lost");
        mailTemplate.setDescription("The characteristics of someone or something");
        mailTemplate.setId(1L);
        mailTemplate.setKeyword("Keyword");
        mailTemplate.setParameters("Parameters");
        mailTemplate.setSubject("Hello from the Dreaming Spires");
        mailTemplate.setVersion(1);
        Optional<MailTemplate> ofResult = Optional.of(mailTemplate);

        MailTemplate mailTemplate2 = new MailTemplate();
        mailTemplate2.preUpdate();
        mailTemplate2.setCharset("UTF-8");
        mailTemplate2.setContent("Not all who wander are lost");
        mailTemplate2.setDescription("The characteristics of someone or something");
        mailTemplate2.setId(1L);
        mailTemplate2.setKeyword("Keyword");
        mailTemplate2.setParameters("Parameters");
        mailTemplate2.setSubject("Hello from the Dreaming Spires");
        mailTemplate2.setVersion(1);
        when(mailTemplateRepository.save(Mockito.<MailTemplate>any())).thenReturn(mailTemplate2);
        when(mailTemplateRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        MailTemplateForm resources = new MailTemplateForm();
        resources.setContent("Not all who wander are lost");
        resources.setDescription("The characteristics of someone or something");
        resources.setId(1L);
        resources.setKeyword("Keyword");
        resources.setParameters("Parameters");
        resources.setSubject("Hello from the Dreaming Spires");
        mailTemplateServiceImpl.update(1L, resources);
        verify(mailTemplateRepository).save(Mockito.<MailTemplate>any());
        verify(mailTemplateRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link MailTemplateServiceImpl#update(Long, MailTemplateForm)}
     */
    @Test
    void testUpdate2() {
        MailTemplate mailTemplate = new MailTemplate();
        mailTemplate.preUpdate();
        mailTemplate.setCharset("UTF-8");
        mailTemplate.setContent("Not all who wander are lost");
        mailTemplate.setDescription("The characteristics of someone or something");
        mailTemplate.setId(1L);
        mailTemplate.setKeyword("Keyword");
        mailTemplate.setParameters("Parameters");
        mailTemplate.setSubject("Hello from the Dreaming Spires");
        mailTemplate.setVersion(1);
        Optional<MailTemplate> ofResult = Optional.of(mailTemplate);
        when(mailTemplateRepository.save(Mockito.<MailTemplate>any()))
                .thenThrow(new BizPromptException("An error occurred"));
        when(mailTemplateRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        MailTemplateForm resources = new MailTemplateForm();
        resources.setContent("Not all who wander are lost");
        resources.setDescription("The characteristics of someone or something");
        resources.setId(1L);
        resources.setKeyword("Keyword");
        resources.setParameters("Parameters");
        resources.setSubject("Hello from the Dreaming Spires");
        assertThrows(BizPromptException.class, () -> mailTemplateServiceImpl.update(1L, resources));
        verify(mailTemplateRepository).save(Mockito.<MailTemplate>any());
        verify(mailTemplateRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link MailTemplateServiceImpl#update(Long, MailTemplateForm)}
     */
    @Test
    void testUpdate3() {
        MailTemplate mailTemplate = mock(MailTemplate.class);
        doNothing().when(mailTemplate).preUpdate();
        doNothing().when(mailTemplate).setVersion(Mockito.<Integer>any());
        doNothing().when(mailTemplate).setCharset(Mockito.<String>any());
        doNothing().when(mailTemplate).setContent(Mockito.<String>any());
        doNothing().when(mailTemplate).setDescription(Mockito.<String>any());
        doNothing().when(mailTemplate).setId(Mockito.<Long>any());
        doNothing().when(mailTemplate).setKeyword(Mockito.<String>any());
        doNothing().when(mailTemplate).setParameters(Mockito.<String>any());
        doNothing().when(mailTemplate).setSubject(Mockito.<String>any());
        mailTemplate.preUpdate();
        mailTemplate.setCharset("UTF-8");
        mailTemplate.setContent("Not all who wander are lost");
        mailTemplate.setDescription("The characteristics of someone or something");
        mailTemplate.setId(1L);
        mailTemplate.setKeyword("Keyword");
        mailTemplate.setParameters("Parameters");
        mailTemplate.setSubject("Hello from the Dreaming Spires");
        mailTemplate.setVersion(1);
        Optional<MailTemplate> ofResult = Optional.of(mailTemplate);

        MailTemplate mailTemplate2 = new MailTemplate();
        mailTemplate2.preUpdate();
        mailTemplate2.setCharset("UTF-8");
        mailTemplate2.setContent("Not all who wander are lost");
        mailTemplate2.setDescription("The characteristics of someone or something");
        mailTemplate2.setId(1L);
        mailTemplate2.setKeyword("Keyword");
        mailTemplate2.setParameters("Parameters");
        mailTemplate2.setSubject("Hello from the Dreaming Spires");
        mailTemplate2.setVersion(1);
        when(mailTemplateRepository.save(Mockito.<MailTemplate>any())).thenReturn(mailTemplate2);
        when(mailTemplateRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        MailTemplateForm resources = new MailTemplateForm();
        resources.setContent("Not all who wander are lost");
        resources.setDescription("The characteristics of someone or something");
        resources.setId(1L);
        resources.setKeyword("Keyword");
        resources.setParameters("Parameters");
        resources.setSubject("Hello from the Dreaming Spires");
        mailTemplateServiceImpl.update(1L, resources);
        verify(mailTemplateRepository).save(Mockito.<MailTemplate>any());
        verify(mailTemplateRepository).findById(Mockito.<Long>any());
        verify(mailTemplate).preUpdate();
        verify(mailTemplate).setVersion(Mockito.<Integer>any());
        verify(mailTemplate).setCharset(Mockito.<String>any());
        verify(mailTemplate, atLeast(1)).setContent(Mockito.<String>any());
        verify(mailTemplate, atLeast(1)).setDescription(Mockito.<String>any());
        verify(mailTemplate).setId(Mockito.<Long>any());
        verify(mailTemplate).setKeyword(Mockito.<String>any());
        verify(mailTemplate, atLeast(1)).setParameters(Mockito.<String>any());
        verify(mailTemplate, atLeast(1)).setSubject(Mockito.<String>any());
    }

    /**
     * Method under test: {@link MailTemplateServiceImpl#deleteById(Long)}
     */
    @Test
    void testDeleteById() {
        doNothing().when(mailTemplateRepository).deleteById(Mockito.<Long>any());
        mailTemplateServiceImpl.deleteById(1L);
        verify(mailTemplateRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link MailTemplateServiceImpl#deleteById(Long)}
     */
    @Test
    void testDeleteById2() {
        doThrow(new BizPromptException("An error occurred")).when(mailTemplateRepository).deleteById(Mockito.<Long>any());
        assertThrows(BizPromptException.class, () -> mailTemplateServiceImpl.deleteById(1L));
        verify(mailTemplateRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link MailTemplateServiceImpl#queryMailTemplateByPage(MailTemplateForm, Pageable)}
     */
    @Test
    void testQueryMailTemplateByPage() {
        MailTemplateForm form = new MailTemplateForm();
        Pageable pageable = PageRequest.of(1, 10);

        MailTemplate template = new MailTemplate();
        template.setId(1L);
        Page<MailTemplate> page = new PageImpl<>(List.of(template), pageable, 100);

        Mockito.when(mailTemplateRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class)))
                .thenReturn(page);

        MailTemplateDTO dto = new MailTemplateDTO();
        Mockito.when(mailTemplateMapper.toDTO(List.of(template)))
                .thenReturn(List.of(dto));

        Page<MailTemplateDTO> result = mailTemplateServiceImpl.queryMailTemplateByPage(form, pageable);

        Mockito.verify(mailTemplateRepository).findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));

        Mockito.verify(mailTemplateMapper).toDTO(List.of(template));

        assertEquals(result.getContent().size(),1);
        assertEquals(result.getContent().get(0),dto);
        assertEquals(result.getTotalElements(),100);
    }

    /**
     * Method under test: {@link MailTemplateServiceImpl#queryAll(MailTemplateQueryCriteria, Pageable)}
     */
    @Test
    void testQueryAll() {
        when(mailTemplateRepository.findAll(Mockito.<Specification<MailTemplate>>any(), Mockito.<Pageable>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        MailTemplateQueryCriteria criteria = new MailTemplateQueryCriteria();
        criteria.setContents("Not all who wander are lost");
        criteria.setDescription("The characteristics of someone or something");
        criteria.setId(1L);
        criteria.setKeyword("Keyword");
        criteria.setParameters("Parameters");
        criteria.setSubject("Hello from the Dreaming Spires");
        assertTrue(mailTemplateServiceImpl.queryAll(criteria, null).toList().isEmpty());
        verify(mailTemplateRepository).findAll(Mockito.<Specification<MailTemplate>>any(), Mockito.<Pageable>any());
    }
}

