package com.jongtix.zerock.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberRequestDto extends User implements OAuth2User {

    private String email;

    private String password;

    private String name;

    private boolean fromSocial;

    private Map<String, Object> attributes;

    public ClubAuthMemberRequestDto(String username, String password, boolean fromSocial, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        this(username, password, fromSocial, authorities);
        this.attributes = attributes;
    }

    public ClubAuthMemberRequestDto(String username, String password, boolean fromSocial, Collection<? extends  GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = username;
        this.password = password;
        this.fromSocial = fromSocial;
    }

//    //AS-IS
//    public ClubAuthMemberRequestDto(String username, String password, boolean fromSocial, Collection<? extends GrantedAuthority> authorities) {
//        super(username, password, authorities);
//        this.email = username;
//        this.fromSocial = fromSocial;
//    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
}
