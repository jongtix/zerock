package com.jongtix.zerock.controller;

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
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GuestbookControllerTest {

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

}