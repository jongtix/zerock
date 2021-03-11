package com.jongtix.zerock.controller;

import com.jongtix.zerock.dto.GuestbookDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public void list(PageRequestDto pageRequestDto, Model model) {
        log.info("list..............");
        log.info("dto.........." + pageRequestDto);

        model.addAttribute("result", guestbookService.getList(pageRequestDto));
    }

//    @GetMapping({ "/", "/list" })
//    public String list() {
//
//        log.info("list...............");
//
//        return "/guestbook/list";
//    }

    @GetMapping("/register")
    public void register() {    //등록 화면 보여주기
        log.info("register...........");
    }

    @PostMapping("/register")
    public String registerPost(GuestbookDto dto, RedirectAttributes redirectAttributes) {   //처리 후 목록 페이지로 이동
        log.info("dto......" + dto);

        Long gno = guestbookService.register(dto);

        redirectAttributes.addFlashAttribute("msg", gno);   //addFlashAttribute: 단 한 번만 데이터를 전달하는 용도로 사용

        return "redirect:/guestbook/list";
    }

    @GetMapping({ "/read", "/modify" })
    public void read(Long gno, @ModelAttribute("requestDto") PageRequestDto requestDto, Model model) {

        log.info("gno: " + gno);
        log.info("requestDto: " + requestDto);

        GuestbookDto dto = guestbookService.read(gno);

        model.addAttribute("dto", dto);

    }

    /**
     * addAttribute vs addFlashAttribute
     * addAttribute: url 뒤에 값이 append & refresh해도 데이터 유지
     * addFlashAttribute: url 뒤에 값이 붙지 않고 refresh하면 데이터 사라짐
     */

    @PostMapping("/remove")
    public String remove(Long gno, RedirectAttributes redirectAttributes) {

        log.info("gno: " + gno);

        guestbookService.remove(gno);

        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";

    }

    @PostMapping("/modify")
    public String modify(GuestbookDto dto, @ModelAttribute("requestDto") PageRequestDto requestDto, RedirectAttributes redirectAttributes) throws Exception {

        log.info("post modify.................");
        log.info("dto: " + dto);

        guestbookService.modify(dto);

        redirectAttributes.addAttribute("page", requestDto.getPage());
        redirectAttributes.addAttribute("type", requestDto.getType());
        redirectAttributes.addAttribute("keyword", requestDto.getKeyword());
        redirectAttributes.addAttribute("gno", dto.getGno());

        return "redirect:/guestbook/read";

    }

}