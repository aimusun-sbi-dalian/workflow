package com.sbi.dl.template.controller;


import com.sbi.dl.compoment.ResponseBase;
import com.sbi.dl.compoment.constant.CommonConstant;
import com.sbi.dl.template.dto.FooDto;
import com.sbi.dl.template.form.CreateFooForm;
import com.sbi.dl.template.form.FooQueryCriteria;
import com.sbi.dl.template.form.UpdateFooForm;
import com.sbi.dl.template.service.FooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/int/v1/Foo")
public class FooController {

    @Autowired private FooService FooService;

    @PostMapping
    public ResponseBase<FooDto> createFoo(@Validated @RequestBody CreateFooForm createForm){
        return ResponseBase.ok(FooService.create(createForm));
    }

    @GetMapping
    public ResponseBase<Page<FooDto>> queryFoo(FooQueryCriteria criteria, @PageableDefault(sort = {CommonConstant.ID}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseBase.ok(FooService.queryAll(criteria, pageable));
    }

    @GetMapping("/all")
    public ResponseBase<List<FooDto>> queryFoo(FooQueryCriteria criteria) {
        return ResponseBase.ok(FooService.queryAll(criteria));
    }

    @GetMapping("/{idOrName}")
    public ResponseBase<FooDto> queryFoo(@PathVariable("idOrName") String idOrName) {
        return ResponseBase.ok(FooService.getFoo(idOrName));
    }

    @PutMapping("/{id}")
    public ResponseBase<Void> updateFoo(
            @Validated @RequestBody UpdateFooForm updateForm, @PathVariable("id") Long id) {
        updateForm.setId(id);
        FooService.update(updateForm);
        return ResponseBase.ok();
    }

    @DeleteMapping("/{id}")
    public ResponseBase<Void> deleteFoo(@PathVariable("id") Long id) {
        FooService.delete(id);
        return ResponseBase.ok();
    }

}
