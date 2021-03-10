package com.jongtix.zerock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jongtix.zerock.domain.guestbook.Guestbook;
import com.jongtix.zerock.domain.guestbook.GuestbookRepository;
import com.jongtix.zerock.dto.GuestbookDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.dto.response.PageResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GuestbookControllerTest {

    @Autowired
    private GuestbookRepository repository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @DisplayName("guestbook_기본_페이지_호출")
    @Test
    void call_context_root_page() throws Exception {
        //given
        String url = "/guestbook/";

        //when
        ResultActions result = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        //then
        result.andDo(print())
                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("/guestbook/list"));
                .andExpect(redirectedUrl("/guestbook/list"));
    }

    @DisplayName("guestbook_리스트_페이지_호출")
    @Test
    void call_list_page() throws Exception {
        //given
        String url = "/guestbook/list";

        //when
        ResultActions result = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        //then
        result.andDo(print())
                .andExpect(status().isOk())
//                .andExpect(view().name("/guestbook/list"));
                .andExpect(model().attributeExists("result"));
    }

    @DisplayName("guestbook_방명록_조회_테스트")
    @Test
    void call_read_test() throws Exception {
        //given
        Long gno = repository.save(
                Guestbook.builder()
                        .title("title")
                        .content("content")
                        .writer("writer")
                        .build()
        ).getGno();

        String url = "/guestbook/read";

        //when
        ResultActions result = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("gno", String.valueOf(gno))
        );

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dto"));
    }

    @DisplayName("guestbook_방명록_수정_테스트")
    @Test
    void call_modify_test() throws Exception {
        //given
        Long gno = repository.save(
                Guestbook.builder()
                        .title("title")
                        .content("content")
                        .writer("writer")
                        .build()
        ).getGno();

        String url = "/guestbook/modify";

        //when
        ResultActions result = mvc.perform(
                get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .param("gno", String.valueOf(gno))
        );

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dto"));
    }

    @DisplayName("방명록_삭제_콜_테스트")
    @Test
    void call_remove_test() throws Exception {
        //given
        Long gno = repository.save(
                Guestbook.builder()
                        .title("title")
                        .content("content")
                        .writer("writer")
                        .build()
        ).getGno();

        String url = "/guestbook/remove";

        //when
        ResultActions result = mvc.perform(
                post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("gno", String.valueOf(gno))
        );

        //then
        result.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/guestbook/list"));
    }

    @DisplayName("방명록_수정_콜_테스트")
    @Test
    void call_post_modify_test() throws Exception {
        //given
        Long gno = repository.save(
                Guestbook.builder()
                        .title("title")
                        .content("content")
                        .writer("writer")
                        .build()
        ).getGno();

        String expectedTitle = "expectedTitle";
        String expectedContent = "expectedContent";

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("gno", String.valueOf(gno));
        params.add("title", expectedTitle);
        params.add("content", expectedContent);
        params.add("writer", "unexpectedWriter");

        String url = "/guestbook/modify";

        //when
        ResultActions result = mvc.perform(
                post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .params(params)
        );

        //then
        result.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attributeExists("gno"))
                .andExpect(view().name("redirect:/guestbook/read"));
    }

}