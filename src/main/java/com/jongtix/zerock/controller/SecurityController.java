package com.jongtix.zerock.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class SecurityController {

    @GetMapping("/sample/all")
    public void exAll() {   //로그인하지 않은 사용자도 접근 가능
        log.info("exAll............");
    }

    @GetMapping("/sample/member")
    public void exMember() {    //로그인한 사용자만 접근 가능
        log.info("exMember...............");
    }

    @GetMapping("/sample/admin")
    public void exAdmin() { //관리자 권한이 있는 사용자만 접근 가능
        log.info("exAdmin.............");
    }

}
