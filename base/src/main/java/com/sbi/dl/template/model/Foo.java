package com.sbi.dl.template.model;

import com.sbi.dl.compoment.jpa.model.DeletableModel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "foo")
public class Foo extends DeletableModel implements UserDetails {

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

    @Builder
    public Foo(
            Long id,
            String username,
            String password,
            int status,
            boolean deleted,
            Long createdAt,
            Long updatedAt,
            Integer version) {
        super(deleted, createdAt, updatedAt, version);
        this.id = id;
        this.username = username;
        this.status = status;
        this.password = password;
    }

    public void delete() {
        this.setDeleted(true);
        username =
                new StringBuilder(username)
                        .append("_")
                        .append(RandomStringUtils.randomAlphanumeric(16))
                        .toString();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
