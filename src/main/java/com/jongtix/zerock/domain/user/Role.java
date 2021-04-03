package com.jongtix.zerock.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

//    USER("ROLE_USER", "일반 사용자");
    ADMIN("ADMIN", "총괄 관리자"),
    MANAGER("MANAGER", "중간 관리 회원"),
    USER("USER", "일반 사용자"); //ROLE_USER cannot start with ROLE_ (it is automatically added)

    private final String key;
    private final String title;

}
