package com.jongtix.zerock.controller;

import com.jongtix.zerock.domain.user.ClubMember;
import com.jongtix.zerock.domain.user.ClubMemberRepository;
import com.jongtix.zerock.domain.user.Note;
import com.jongtix.zerock.domain.user.NoteRepository;
import com.jongtix.zerock.dto.request.NoteRequestDto;
import com.jongtix.zerock.dto.response.NoteResponseDto;
import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NoteControllerTest {

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private NoteRepository noteRepository;

    @LocalServerPort
    private int port;

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @AfterEach
    void tearDown() {
        noteRepository.deleteAll();
        clubMemberRepository.deleteAll();
    }

    @DisplayName("등록_테스트")
    @Test
    void register() throws Exception {
        //given
        String title = "title";
        String content = "content";
        String writerEmail = "jongtix1145@gmail.com";
        clubMemberRepository.save(
                ClubMember.builder()
                        .email(writerEmail)
                        .build()
        );

        NoteResponseDto dto = NoteResponseDto.builder()
                .title(title)
                .content(content)
                .writerEmail(writerEmail)
                .build();

        String url = "http://localhost:" + port + "/notes/";

        //when
        ResultActions resultActions = mvc.perform(
                post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(dto))
        );

        //then
        resultActions
                .andExpect(status().isCreated());
    }

    @DisplayName("가져오기_테스트")
    @Test
    void read() throws Exception {
        //given
        String title = "title";
        String content = "content";
        String writerEmail = "jongtix1145@gmail.com";

        Long num = noteRepository.save(
                Note.builder()
                        .title(title)
                        .content(content)
                        .writer(
                                clubMemberRepository.save(
                                        ClubMember.builder()
                                                .email(writerEmail)
                                                .build()
                                )
                        )
                        .build()
        ).getNum();

        String url = "http://localhost:" + port + "/notes/{num}";

        //when
        ResultActions resultActions = mvc.perform(
                get(url, num)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.writerEmail").value(writerEmail));

    }

    @DisplayName("사용자의_모든_노트_가져오기_테스트")
    @Test
    void getList() throws Exception {
        String title1 = "title1";
        String content1 = "content1";
        String writerEmail = "jongtix1145@gmail.com";
        ClubMember writer = clubMemberRepository.save(
                ClubMember.builder()
                        .email(writerEmail)
                        .build()
        );

        noteRepository.save(
                Note.builder()
                        .title(title1)
                        .content(content1)
                        .writer(writer)
                        .build()
        );

        String title2 = "title2";
        String content2 = "content2";

        noteRepository.save(
                Note.builder()
                        .title(title2)
                        .content(content2)
                        .writer(writer)
                        .build()
        );

        String url = "http://localhost:" + port + "/notes/all";

        //when
        ResultActions resultActions = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("email", writerEmail)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title").value(title1))
                .andExpect(jsonPath("$.[0].content").value(content1))
                .andExpect(jsonPath("$.[0].writerEmail").value(writerEmail))
                .andExpect(jsonPath("$.[1].title").value(title2))
                .andExpect(jsonPath("$.[1].content").value(content2))
                .andExpect(jsonPath("$.[1].writerEmail").value(writerEmail));
    }

    @DisplayName("노트_삭제_테스트")
    @Test
    void remove() throws Exception {
        String title = "title";
        String content = "content";
        String writerEmail = "jongtix1145@gmail.com";

        Long num = noteRepository.save(
                Note.builder()
                        .title(title)
                        .content(content)
                        .writer(
                                clubMemberRepository.save(
                                        ClubMember.builder()
                                                .email(writerEmail)
                                                .build()
                                )
                        )
                        .build()
        ).getNum();

        String url = "http://localhost:" + port + "/notes/{num}";

        //when
        ResultActions resultActions = mvc.perform(
                delete(url, num)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        //then
        resultActions
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").value("removed"));
    }

    @DisplayName("노트_수정_테스트")
    @Test
    void modify() throws Exception {
        String title = "title";
        String content = "content";
        String writerEmail = "jongtix1145@gmail.com";

        Long num = noteRepository.save(
                Note.builder()
                        .title(title)
                        .content(content)
                        .writer(
                                clubMemberRepository.save(
                                        ClubMember.builder()
                                                .email(writerEmail)
                                                .build()
                                )
                        )
                        .build()
        ).getNum();

        String expectedTitle = "expectedTitle";
        String expectedContent = "expectedContent";

        NoteRequestDto dto = NoteRequestDto.builder()
                .num(num)
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/notes/{num}";

        //when
        ResultActions resultActions = mvc.perform(
                put(url, num)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(dto))
        );

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value("modified"));

        Note note = noteRepository.findById(num).orElseThrow(() -> new NoSuchElementException("해당하는 노트가 없습니다. num: " + num));
        assertThat(note.getTitle()).isEqualTo(expectedTitle);
        assertThat(note.getContent()).isEqualTo(expectedContent);
    }
}