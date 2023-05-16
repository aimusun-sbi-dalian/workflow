package com.sbi.expo.bo.base.rbac.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

/**
 * @author Ming.G
 */
@Data
@NoArgsConstructor
public class AuthStaffDTO implements UserDetails {

    private Long id;
    private String name;
    private String password;
    private Integer status;
    private Long roleId;
    private List<String> permissions;

    public AuthStaffDTO(
            Long id,
            String name,
            String password,
            Integer status,
            Long roleId,
            List<String> permissions) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.status = status;
        this.roleId = roleId;
        this.permissions = permissions;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return !CollectionUtils.isEmpty(permissions)
                ? permissions.stream().map(AuthStaffAuthority::new).collect(Collectors.toSet())
                : new HashSet<>(0);
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return 0 != status;
    }

    public static class AuthStaffAuthority implements GrantedAuthority {
        private final String permission;

        public AuthStaffAuthority(String permission) {
            this.permission = permission;
        }

        @Override
        public String getAuthority() {
            return permission;
        }
    }
}
