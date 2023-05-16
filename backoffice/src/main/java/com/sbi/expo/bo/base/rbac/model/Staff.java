package com.sbi.expo.bo.base.rbac.model;

import com.sbi.expo.bo.base.jpa.model.DeletableModel;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author Ming.G
 */
@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "mgt_staff")
public class Staff extends DeletableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    /** 0-disable,1-enable default 1 */
    @Column(nullable = false)
    private Integer status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "mgt_staff_role",
            joinColumns = {@JoinColumn(name = "staff_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Role role;

    @Builder
    public Staff(
            Long id,
            String username,
            String password,
            int status,
            Role role,
            boolean deleted,
            Long createdAt,
            Long updatedAt,
            Integer version) {
        super(deleted, createdAt, updatedAt, version);
        this.id = id;
        this.username = username;
        this.status = status;
        this.password = password;
        this.role = role;
    }

    public void delete() {
        this.setDeleted(true);
        username =
                new StringBuilder(username)
                        .append("_")
                        .append(RandomStringUtils.randomAlphanumeric(16))
                        .toString();
    }
}
