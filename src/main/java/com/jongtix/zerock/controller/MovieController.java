package com.jongtix.zerock.controller;

import com.jongtix.zerock.dto.request.MovieRequestDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.dto.response.MovieResponseDto;
import com.jongtix.zerock.service.moviereview.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MovieController {

    private final MovieService service;

    @GetMapping("/movie/register")
    public void register() {}

    @PostMapping("/movie/register")
    public String register(MovieRequestDto requestDto, RedirectAttributes redirectAttributes) {

        log.info("movieRequestDto: " + requestDto);

        Long mno = service.register(requestDto);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/movie/list";
    }

    @GetMapping("/movie/list")
    public void list(PageRequestDto requestDto, Model model) {

        log.info("requestDto: " + requestDto);

        model.addAttribute("result", service.getList(requestDto));

    }

    @GetMapping({ "/movie/read", "/movie/modify" })
    public void read(long mno, @ModelAttribute("requestDto") PageRequestDto requestDto, Model model) {

        log.info("requestDto: " + requestDto);

        MovieResponseDto responseDto = service.getMovie(mno);

        model.addAttribute("dto", responseDto);

    }

}
