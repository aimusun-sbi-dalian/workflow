package com.sbi.expo.bo.base.rbac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sbi.expo.bo.base.jpa.model.DeletableModel;
import java.util.List;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

/**
 * Permission
 *
 * @author Ming.G
 * @date 2022-05-24
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "mgt_permission")
@Where(clause = "deleted = 0")
public class Permission extends DeletableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private List<Permission> children;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "pid", insertable = false, updatable = false)
    private Permission parent;

    private Long pid;

    private String permission;

    private String name;
    /** 0-permission,1-group */
    private Integer type;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private List<Role> roles;

    @Builder
    public Permission(
            Long id,
            Long parentId,
            String permission,
            String name,
            Integer type,
            boolean deleted,
            Long createdAt,
            Long updatedAt,
            Integer version) {
        super(deleted, createdAt, updatedAt, version);
        this.id = id;
        this.pid = parentId;
        this.permission = permission;
        this.name = name;
        this.type = type;
    }
}
