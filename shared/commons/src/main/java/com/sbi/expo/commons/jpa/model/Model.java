package com.sbi.expo.commons.jpa.model;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@MappedSuperclass
@NoArgsConstructor
public abstract class Model implements Serializable {

    Long createdAt;
    Long updatedAt;
    @Version @Setter Integer version;

    public Model(Long createdAt, Long updatedAt, Integer version) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    /** Update the updatedAt and createdAt before insert into DB */
    @PrePersist
    public void prePersist() {
        long time = System.currentTimeMillis();
        if (this.createdAt == null) {
            createdAt = time;
        }
        updatedAt = time;
    }

    /** Update the updatedAt before update into DB */
    @PreUpdate
    public void preUpdate() {
        updatedAt = System.currentTimeMillis();
    }
}
