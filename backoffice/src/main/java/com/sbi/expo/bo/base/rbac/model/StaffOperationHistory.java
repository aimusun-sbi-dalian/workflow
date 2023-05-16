package com.sbi.expo.bo.base.rbac.model;

import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * StaffOperationHistory
 *
 * @author Ming.G
 * @date 2022-05-24
 */
@Getter
@Setter
@Entity
@Table(name = "his_staff_operation")
public class StaffOperationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long staffId;
    private String staffName;
    private String operation;
    private Long createdAt;

    public StaffOperationHistory() {
        this.createdAt = System.currentTimeMillis();
    }

    @Builder
    public StaffOperationHistory(
            Long id, Long staffId, String staffName, String operation, Long createdAt) {
        this.id = id;
        this.staffId = staffId;
        this.staffName = staffName;
        this.operation = operation;
        this.createdAt = null == createdAt ? System.currentTimeMillis() : createdAt;
    }
}
