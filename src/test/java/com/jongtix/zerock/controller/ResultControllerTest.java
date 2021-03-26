package com.jongtix.zerock.controller;

import com.jongtix.zerock.service.bunjang.ResultService;
import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResultControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private ResultService resultService;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("결과_가져오기_sort_없을_때")
    @Test
    void getList() throws Exception {
        //given
        resultService.service();

        String url = "http://localhost:" + port + "/api/v1/event-summaries";

        //when
        ResultActions resultActions = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        resultActions.andExpect(status().isOk());
    }

    @DisplayName("결과_가져오기_sort_있을_때")
    @Test
    void getListWithSort() throws Exception {
        //given
        resultService.service();

        String url = "http://localhost:" + port + "/api/v1/event-summaries";

        //when
        ResultActions resultActions = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("sort", "IssueCommentEvent")
        );

        resultActions.andExpect(status().isOk());
    }
}