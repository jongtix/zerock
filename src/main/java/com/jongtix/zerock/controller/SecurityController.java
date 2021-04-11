package com.jongtix.zerock.controller;

import com.jongtix.zerock.dto.request.ClubAuthMemberRequestDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class SecurityController {

    @PreAuthorize("permitAll()")    //TO-BE
                                    //@PreAuthorize적용 후
                                    //모든 사용자가 접근 가능
    @GetMapping("/sample/all")
    public void exAll() {   //로그인하지 않은 사용자도 접근 가능
        log.info("exAll............");
    }

//    @PreAuthorize("#clubAuthMember != null && #clubAuthMember.username eq \"user95@zerock.org\"")   //@PreAuthorize의 value 표현식은 '#'과 같은 특별한 기호나 authentication 같은 내장 변수를 이용할 수 있음
    @GetMapping("/sample/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberRequestDto requestDto) {    //로그인한 사용자만 접근 가능
                                                                                            //AuthenticationPrincipal: getPrincipal 메서드를 통해서 Object 타입으로 반환 가능하여 별도의 캐스팅 작업 없이 ClubAuthMemberRequestDto 타입을 사용할 수 있음
        log.info("exMember...............");

        log.info(requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")   //TO-BE
                                        //@PreAuthorize적용 후
                                        //ADMIN 권한이 있는 사용자만
    @GetMapping("/sample/admin")
    public void exAdmin() { //관리자 권한이 있는 사용자만 접근 가능
        log.info("exAdmin.............");
    }

}
