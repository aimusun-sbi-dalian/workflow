package com.sbi.dl.template.service;

import com.sbi.dl.template.dto.FooDto;
import com.sbi.dl.template.form.CreateFooForm;
import com.sbi.dl.template.form.FooQueryCriteria;
import com.sbi.dl.template.form.UpdateFooForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FooService {

    /**
     * create Foo
     * @param createForm create Foo form
     * @return FooDto
     */
    FooDto create(CreateFooForm createForm);

    /**
     * get Foo list with page
     * @param criteria query condition
     * @param pageable page condition
     * @return Page<FooDto>
     */
    Page<FooDto> queryAll(FooQueryCriteria criteria, Pageable pageable);

    /**
     * get all Foo list
     * @param criteria query condition
     * @return List<FooDto>
     */
    List<FooDto> queryAll(FooQueryCriteria criteria);

    /**
     * get Foo by idOrName
     * @param idOrName
     * @return FooDto
     */
    FooDto getFoo(String idOrName);

    /**
     * update Foo
     * @param updateForm update Foo form
     */
    void update(UpdateFooForm updateForm);

    /**
     * delete Foo by id
     * @param id
     */
    void delete(Long id);


}
