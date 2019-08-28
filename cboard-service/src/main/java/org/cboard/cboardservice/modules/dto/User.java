package org.cboard.cboardservice.modules.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by yfyuan on 2016/9/29.
 */
@Data
public class User extends org.springframework.security.core.userdetails.User {

    private String userId;
    private String company;
    private String department;
    private String name;

    public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public User(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

}

