package com.jongtix.zerock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jongtix.zerock.dto.request.MovieImageRequestDto;
import com.jongtix.zerock.dto.request.MovieRequestDto;
import com.jongtix.zerock.utils.Constants;
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
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("영화_등록_테스트")
    @Test
    void registerPost() throws Exception {
        //given
        String url = "http://localhost:" + port + "/movie/register";

        String title = "title";
        List<MovieImageRequestDto> movieImageRequestDtoList = new ArrayList<>();

        String uuid1 = UUID.randomUUID().toString();
        String imgName1 = "imgName1";
        String path1 = "path1";
        movieImageRequestDtoList.add(
                MovieImageRequestDto.builder()
                        .uuid(uuid1)
                        .imgName(imgName1)
                        .path(path1)
                        .build()
        );

        String uuid2 = UUID.randomUUID().toString();
        String imgName2 = "imgName2";
        String path2 = "path2";
        movieImageRequestDtoList.add(
                MovieImageRequestDto.builder()
                        .uuid(uuid2)
                        .imgName(imgName2)
                        .path(path2)
                        .build()
        );

        MovieRequestDto requestDto = MovieRequestDto.builder()
                .title(title)
                .movieImageRequestDtoList(movieImageRequestDtoList)
                .build();

        //when
        ResultActions resultActions = mvc.perform(
                post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(Constants.DEFAULT_ENCODING)
                .content(new ObjectMapper().writeValueAsString(requestDto))
        );

        //then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("msg"))
                .andExpect(view().name("redirect:/movie/list"));
    }

    @DisplayName("영화_등록_화면_조회_테스트")
    @Test
    void registerGet() throws Exception {
        //given
        String url = "http://localhost:" + port + "/movie/register";

        //when
        ResultActions resultActions = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }
}