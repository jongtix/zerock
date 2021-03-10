package com.jongtix.zerock.controller;

import com.jongtix.zerock.dto.ThymeleafDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sample")
@Log4j2 //Lombok 기능
public class ThymeleafController {

    @GetMapping("/ex1")
    public void ex1() { //Thymeleaf는 기본적으로 resources/templates/ 하위 경로의 URI.html 파일 탐색

        log.info("ex1..................");

    }

    @GetMapping({"/ex2", "/exLink"})   //{} 사용: 하나 이상의 URL 지정 가능
    public void exModel(Model model) {

        List<ThymeleafDto> list = IntStream.rangeClosed(1, 20)
                .asLongStream()
                .mapToObj(i -> {
                    return ThymeleafDto.builder()
                            .sno(i)
                            .first("First.." + i)
                            .last("Last.." + i)
                            .regTime(LocalDateTime.now())
                            .build();
                }).collect(Collectors.toList());

        model.addAttribute("list", list);
    }

    @GetMapping({"/exInline"})
    public String exInline(RedirectAttributes redirectAttributes) {

        log.info("exInline............");

        ThymeleafDto dto = ThymeleafDto.builder()
                .sno(100L)
                .first("First.." + 100)
                .last("Last.." + 100)
                .regTime(LocalDateTime.now())
                .build();

        redirectAttributes.addFlashAttribute("result", "success");
        redirectAttributes.addFlashAttribute("dto", dto);

        return "redirect:/sample/ex3";
    }

    @GetMapping("ex3")
    public void ex3() {

        log.info("ex3....................");

    }

    @GetMapping({"/exLayout1", "/exLayout2", "/exTemplate", "/exSidebar"})
    public void exLayout1() {

        log.info("exLayout................");

    }

}
