package com.sbi.expo.bo.base.rbac.service;

/**
 * StaffOperationHistoryService
 *
 * @author Ming.G
 * @date 2022-05-24
 */
public interface StaffOperationHistoryService {
    void history(String operation);

    void historyById(long staffId, String staffName, String operation);
}
