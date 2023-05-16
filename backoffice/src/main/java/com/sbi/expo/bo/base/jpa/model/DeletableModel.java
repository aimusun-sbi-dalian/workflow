package com.sbi.expo.bo.base.jpa.model;

import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public abstract class DeletableModel extends Model {

    boolean deleted;

    public DeletableModel(boolean deleted, Long createdAt, Long updatedAt, Integer version) {
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }
}
