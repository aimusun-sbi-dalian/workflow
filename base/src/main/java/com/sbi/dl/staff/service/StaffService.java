package com.sbi.dl.staff.service;

import com.sbi.dl.staff.dto.StaffDto;
import com.sbi.dl.staff.dto.StaffQueryCriteria;
import com.sbi.dl.staff.form.CreateStaffForm;
import com.sbi.dl.staff.form.UpdateStaffForm;
import com.sbi.dl.staff.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * StaffService
 *
 * @author Ming.G
 * @date 2022-05-24
 */
public interface StaffService {

    Optional<Staff> getStaffByUsername(String username);

    /**
     * create Staff
     * @param createForm create Staff form
     * @return StaffDto
     */
    StaffDto create(CreateStaffForm createForm);

    /**
     * get Staff list with page
     * @param criteria query condition
     * @param pageable page condition
     * @return Page<StaffDto>
     */
    Page<StaffDto> queryAll(StaffQueryCriteria criteria, Pageable pageable);

    /**
     * get all Staff list
     * @param criteria query condition
     * @return List<StaffDto>
     */
    List<StaffDto> queryAll(StaffQueryCriteria criteria);

    /**
     * get Staff by idOrName
     * @param idOrName
     * @return StaffDto
     */
    StaffDto getStaff(String idOrName);

    /**
     * update Staff
     * @param updateForm update Staff form
     */
    void update(UpdateStaffForm updateForm);

    /**
     * delete Staff by id
     * @param id
     */
    void delete(Long id);


}
