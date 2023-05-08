package com.sbi.dl.staff.controller;


import com.sbi.dl.compoment.ResponseBase;
import com.sbi.dl.compoment.constant.CommonConstant;
import com.sbi.dl.staff.dto.StaffDto;
import com.sbi.dl.staff.dto.StaffQueryCriteria;
import com.sbi.dl.staff.form.CreateStaffForm;
import com.sbi.dl.staff.form.UpdateStaffForm;
import com.sbi.dl.staff.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * StaffController
 *
 * @author Ming.G
 * @date 2022-05-23
 */
@RestController
@RequestMapping("/int/v1/staff")
public class StaffController {

    @Autowired private StaffService staffService;

    @PostMapping
    @Bean
    @Scope(value = )
    public ResponseBase<StaffDto> createStaff(@Validated @RequestBody CreateStaffForm createForm){
        return ResponseBase.ok(staffService.create(createForm));
    }

    @GetMapping
    public ResponseBase<Page<StaffDto>> queryStaff(StaffQueryCriteria criteria, @PageableDefault(sort = {CommonConstant.ID}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseBase.ok(staffService.queryAll(criteria, pageable));
    }

    @GetMapping("/all")
    public ResponseBase<List<StaffDto>> queryStaff(StaffQueryCriteria criteria) {
        return ResponseBase.ok(staffService.queryAll(criteria));
    }

    @GetMapping("/{idOrName}")
    public ResponseBase<StaffDto> queryStaff(@PathVariable("idOrName") String idOrName) {
        return ResponseBase.ok(staffService.getStaff(idOrName));
    }

    @PutMapping("/{id}")
    public ResponseBase<Void> updateStaff(
            @Validated @RequestBody UpdateStaffForm updateForm, @PathVariable("id") Long id) {
        updateForm.setId(id);
        staffService.update(updateForm);
        return ResponseBase.ok();
    }

    @DeleteMapping("/{id}")
    public ResponseBase<Void> deleteStaff(@PathVariable("id") Long id) {
        staffService.delete(id);
        return ResponseBase.ok();
    }

}
