package com.example.usermicroservice.security;

import com.example.usermicroservice.entity.Role;
import com.example.usermicroservice.entity.User;
import com.example.usermicroservice.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrincipal implements UserDetails {

    private Long id;
    private String email;

    private transient String password;
    private transient User user;
    private Set<GrantedAuthority> authorities;


    public static UserPrincipal createSuperUser() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(SecurityUtils.convertToAuthority(String.valueOf(Role.ADMIN)));

        return UserPrincipal.builder().id(-1L).email("system-administrator").authorities(authorities).build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return user.isEnabled();
    }
}
