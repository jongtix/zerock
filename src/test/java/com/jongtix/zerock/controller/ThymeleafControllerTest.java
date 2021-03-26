package com.jongtix.zerock.controller;

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
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ThymeleafControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @DisplayName("ex1_호출")
    @Test
    void call_ex1_test() throws Exception {
        //given
        String url = "/sample/ex1";

        //when
        ResultActions result = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andDo(print())
                .andExpect(status().isOk());

    }

    @DisplayName("ex2_호출")
    @Test
    void call_ex2_test() throws Exception {
        //given
        String url = "/sample/ex2";

        //when
        ResultActions result = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("list"))
                .andExpect(model().size(1));
    }

    @DisplayName("exInline_호출")
    @Test
    void call_exInline_test() throws Exception {
        //given
        String url = "/sample/exInline";

        //when
        ResultActions result = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("result"))
                .andExpect(flash().attributeExists("dto"))
                .andExpect(view().name("redirect:/sample/ex3"))
                .andExpect(redirectedUrl("/sample/ex3"));
    }

    @DisplayName("exLink_호출")
    @Test
    void call_exLink_test() throws Exception {
        //given
        String url = "/sample/exLink";

        //when
        ResultActions result = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("list"));
    }
}