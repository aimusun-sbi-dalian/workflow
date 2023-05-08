package com.sbi.dl.staff.service.impl;


import com.sbi.dl.base.constant.MessageConstant;
import com.sbi.dl.compoment.constant.CommonConstant;
import com.sbi.dl.compoment.exception.BizPromptException;
import com.sbi.dl.compoment.utils.BitsBeanUtils;
import com.sbi.dl.compoment.utils.BitsQueryUtil;
import com.sbi.dl.staff.dto.StaffDto;
import com.sbi.dl.staff.dto.StaffQueryCriteria;
import com.sbi.dl.staff.form.CreateStaffForm;
import com.sbi.dl.staff.form.UpdateStaffForm;
import com.sbi.dl.staff.mapstruct.StaffMapper;
import com.sbi.dl.staff.model.Staff;
import com.sbi.dl.staff.repository.StaffRepository;
import com.sbi.dl.staff.service.StaffService;
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

/**
 * StaffServiceImpl
 *
 * @author Ming.G
 * @date 2022-05-24
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class StaffServiceImpl implements StaffService {

    @Autowired private StaffMapper staffMapper;
    @Autowired private StaffRepository staffRepository;

    @Override
    public Optional<Staff> getStaffByUsername(String username) {
        return staffRepository.findByDeletedFalseAndUsername(username);
    }

    private Optional<Staff> getStaffById(Long staffId) {
        return staffRepository.findById(staffId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StaffDto create(CreateStaffForm form) {
        Staff Staff = new Staff();
        BitsBeanUtils.shallowCopy(form, Staff);
        return staffMapper.toDTO(staffRepository.save(Staff));
    }

    @Override
    public Page<StaffDto> queryAll(StaffQueryCriteria criteria, Pageable pageable){
        Page<Staff> page =
                staffRepository.findAll(
                (root, query, builder) ->
                        BitsQueryUtil.getPredicate(root, criteria, builder),
                pageable);
        return page.map(staffMapper::toDTO);
    }

    @Override
    public List<StaffDto> queryAll(StaffQueryCriteria criteria) {
        return staffMapper.toDTO(
                staffRepository.findAll(
                (root, query, builder) ->
                        BitsQueryUtil.getPredicate(root, criteria, builder),
                Sort.by(Sort.Order.desc(CommonConstant.ID))));
    }

    @Override
    public StaffDto getStaff(String idOrName) {
        if (StringUtils.isBlank(idOrName)) {
            throw new BizPromptException(MessageConstant.CODE_10204);
        }
        Staff staff =
                StringUtils.isNumeric(idOrName)
                        ? getStaffById(Long.valueOf(idOrName))
                        .orElseThrow(
                                () -> new BizPromptException(MessageConstant.CODE_10204))
                        : getStaffByUsername(idOrName)
                        .orElseThrow(
                                () -> new BizPromptException(MessageConstant.CODE_10204));
        return staffMapper.toDTO(staff);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UpdateStaffForm form) {
        Staff staff = staffRepository.findByIdAndVersion(form.getId(), form.getVersion()).orElseThrow(OptimisticLockException::new);
        BitsBeanUtils.shallowCopy(form, staff);
        staffRepository.save(staff);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        staffRepository.deleteById(id);
    }

}
