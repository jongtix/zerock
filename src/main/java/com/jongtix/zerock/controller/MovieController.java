package com.jongtix.zerock.controller;

import com.jongtix.zerock.dto.request.MovieRequestDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.dto.response.MovieResponseDto;
import com.jongtix.zerock.service.moviereview.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/movie/register")
    public void register() {}

    @PostMapping("/movie/register")
    public String register(MovieRequestDto requestDto, RedirectAttributes redirectAttributes) {

        log.info("movieRequestDto: " + requestDto);

        Long mno = movieService.register(requestDto);

        redirectAttributes.addFlashAttribute("msg", mno);

        return "redirect:/movie/list";
    }

    @GetMapping("/movie/list")
    public void list(PageRequestDto requestDto, Model model) {

        log.info("requestDto: " + requestDto);

        model.addAttribute("result", movieService.getList(requestDto));

    }

    @GetMapping({ "/movie/read", "/movie/modify" })
    public void read(Long mno, @ModelAttribute("requestDto") PageRequestDto requestDto, Model model) {

        log.info("requestDto: " + requestDto);

        MovieResponseDto responseDto = movieService.getMovie(mno);

        model.addAttribute("dto", responseDto);

    }
    
    @DeleteMapping("/movie/remove")
    public String remove(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, Long mno, RedirectAttributes redirectAttributes) {
        log.info("mno: " + mno);

        movieService.removeMovie(mno);

        redirectAttributes.addFlashAttribute("msg", mno);
        redirectAttributes.addAttribute("page", pageRequestDto.getPage());
        redirectAttributes.addAttribute("type", pageRequestDto.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDto.getKeyword());

        return "redirect:/movie/list";
    }

    @PatchMapping("/movie/modify")
    public String modify(MovieRequestDto movieRequestDto, @ModelAttribute("requestDto") PageRequestDto pageRequestDto, RedirectAttributes redirectAttributes) {
        log.info("post modify...........");
        log.info("movieRequestDto: " + movieRequestDto);
        log.info("pageRequestDto: " + pageRequestDto);

        Long mno = movieService.modify(movieRequestDto);

        redirectAttributes.addAttribute("mno", mno);
        redirectAttributes.addAttribute("page", pageRequestDto.getPage());
        redirectAttributes.addAttribute("type", pageRequestDto.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDto.getKeyword());

        return "redirect:/movie/read";
    }

}
