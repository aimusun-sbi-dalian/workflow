package com.sbi.expo.bo.base.rbac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sbi.expo.bo.base.jpa.model.DeletableModel;
import java.util.List;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Role
 *
 * @author Ming.G
 * @date 2022-05-24
 */
@Entity
@Getter
@Setter
@Table(name = "mgt_role")
@NoArgsConstructor
public class Role extends DeletableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private List<Staff> staffs;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "mgt_role_permission",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")})
    private List<Permission> permissions;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "mgt_role_menu",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")})
    private List<Menu> menus;

    @Builder
    public Role(
            Long id,
            String name,
            List<Staff> staffs,
            List<Permission> permissions,
            boolean deleted,
            Long createdAt,
            Long updatedAt,
            Integer version) {
        super(deleted, createdAt, updatedAt, version);
        this.id = id;
        this.name = name;
        this.staffs = staffs;
        this.permissions = permissions;
    }

    public void delete() {
        this.setDeleted(true);
        name =
                new StringBuilder(name)
                        .append("_")
                        .append(RandomStringUtils.randomAlphanumeric(16))
                        .toString();
    }

    @JsonIgnore
    public List<String> getPermissionKeys() {
        return this.permissions.stream().map(Permission::getPermission).toList();
    }

    @JsonIgnore
    public List<Long> getPermissionsId() {
        return this.permissions.stream().map(Permission::getId).toList();
    }

    @JsonIgnore
    public List<Long> getMenusId() {
        return this.menus.stream().map(Menu::getId).toList();
    }
}
