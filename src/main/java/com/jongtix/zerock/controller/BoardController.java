package com.jongtix.zerock.controller;

import com.jongtix.zerock.dto.request.BoardRequestDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.dto.response.BoardResponseDto;
import com.jongtix.zerock.service.board.BoardService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
//@RequestMapping("/board")
@Log4j2
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/list")
    public void list(PageRequestDto requestDto, Model model) {

        log.info("list...." + requestDto);

        model.addAttribute("result", boardService.getList(requestDto));

    }

    @GetMapping("/board/register")
    public void register(PageRequestDto requestDto) {

        log.info("register get...........");

    }

    @PostMapping("/board/register")
    public String registerPost(BoardRequestDto requestDto, RedirectAttributes redirectAttributes) {
        log.info("requestDto........" + requestDto);

        Long bno = boardService.register(requestDto);

        log.info("bno: " + bno);

        redirectAttributes.addFlashAttribute("msg", bno);

        return "redirect:/board/list";
    }

    @GetMapping({ "/board/read", "/board/modify" })
    public void read(@ModelAttribute("requestDto") PageRequestDto requestDto, Long bno, Model model) {
        log.info("bno: " + bno);

        BoardResponseDto responseDto = boardService.getBoard(bno);

        log.info("boardResponseDto............." + responseDto);

        model.addAttribute("dto", responseDto);
    }

    @DeleteMapping("/board/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes) {
        log.info("bno: " + bno);

        boardService.removeWithReplies(bno);

        redirectAttributes.addFlashAttribute("msg", bno);

        return "redirect:/board/list";
    }

    @PatchMapping("/board/modify")
    public String modify(BoardRequestDto boardRequestDto, @ModelAttribute("requestDto") PageRequestDto pageRequestDto, RedirectAttributes redirectAttributes) {
        log.info("post modify...........");
        log.info("boardRequestDto: " + boardRequestDto);
        log.info("pageRequestDto: " + pageRequestDto);

        boardService.modify(boardRequestDto);

        redirectAttributes.addAttribute("page", pageRequestDto.getPage());
        redirectAttributes.addAttribute("type", pageRequestDto.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDto.getKeyword());

        return "redirect:/board/read";
    }

}
