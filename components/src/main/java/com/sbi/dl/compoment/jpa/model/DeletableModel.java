package com.sbi.dl.compoment.jpa.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

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
