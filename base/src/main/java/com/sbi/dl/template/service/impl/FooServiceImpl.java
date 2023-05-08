package com.sbi.dl.template.service.impl;


import com.sbi.dl.base.constant.MessageConstant;
import com.sbi.dl.compoment.constant.CommonConstant;
import com.sbi.dl.compoment.exception.BizPromptException;
import com.sbi.dl.compoment.utils.BitsBeanUtils;
import com.sbi.dl.compoment.utils.BitsQueryUtil;
import com.sbi.dl.template.dto.FooDto;
import com.sbi.dl.template.form.CreateFooForm;
import com.sbi.dl.template.form.FooQueryCriteria;
import com.sbi.dl.template.form.UpdateFooForm;
import com.sbi.dl.template.mapstruct.FooMapper;
import com.sbi.dl.template.model.Foo;
import com.sbi.dl.template.repository.FooRepository;
import com.sbi.dl.template.service.FooService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class FooServiceImpl implements FooService {

    @Autowired private FooMapper fooMapper;
    @Autowired private FooRepository fooRepository;


    private Optional<Foo> getFooById(Long FooId) {
        return fooRepository.findById(FooId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FooDto create(CreateFooForm form) {
        Foo Foo = new Foo();
        BitsBeanUtils.shallowCopy(form, Foo);
        return fooMapper.toDTO(fooRepository.save(Foo));
    }

    @Override
    public Page<FooDto> queryAll(FooQueryCriteria criteria, Pageable pageable){
        Page<Foo> page =
                fooRepository.findAll(
                (root, query, builder) ->
                        BitsQueryUtil.getPredicate(root, criteria, builder),
                pageable);
        return page.map(fooMapper::toDTO);
    }

    @Override
    public List<FooDto> queryAll(FooQueryCriteria criteria) {
        return fooMapper.toDTO(
                fooRepository.findAll(
                (root, query, builder) ->
                        BitsQueryUtil.getPredicate(root, criteria, builder),
                Sort.by(Sort.Order.desc(CommonConstant.ID))));
    }

    @Override
    public FooDto getFoo(String idOrName) {
        if (StringUtils.isBlank(idOrName)) {
            throw new BizPromptException(MessageConstant.CODE_10204);
        }
        Foo Foo = getFooById(Long.valueOf(idOrName))
                        .orElseThrow(
                                () -> new BizPromptException(MessageConstant.CODE_10204));
        return fooMapper.toDTO(Foo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UpdateFooForm form) {
        Foo Foo = fooRepository.findById(form.getId()).orElseThrow(OptimisticLockException::new);
        BitsBeanUtils.shallowCopy(form, Foo);
        fooRepository.save(Foo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        fooRepository.deleteById(id);
    }

}
