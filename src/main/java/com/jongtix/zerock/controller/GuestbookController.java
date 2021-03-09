package com.jongtix.zerock.controller;

import com.jongtix.zerock.dto.GuestbookDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor
class GuestbookController {

    private final GuestbookService guestbookService;

    @GetMapping("/")
    public String index() {
        return "redirect:/guestbook/list";
    }

    /**
     * Spring MVC는 파라미터를 자동으로 수집해주는 기능이 있음
     * ex) 화면에서 page와 size라는 파라미터를 전달하면 PageRequestDto 객체로 자동으로 수집 됨
     */
    @GetMapping("/list")
    public void list(PageRequestDto dto, Model model) {
        log.info("list..............");

        model.addAttribute("result", guestbookService.getList(dto));
    }

//    @GetMapping({ "/", "/list" })
//    public String list() {
//
//        log.info("list...............");
//
//        return "/guestbook/list";
//    }

}