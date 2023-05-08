package com.sbi.dl.staff.type;

/**
 * StaffStatus
 *
 * @author Ming.G
 * @date 2022-06-06
 */
public enum StaffStatus {
    /** StaffStatus */
    DISABLE(0),
    ENABLE(1);

    private final int value;

    StaffStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
