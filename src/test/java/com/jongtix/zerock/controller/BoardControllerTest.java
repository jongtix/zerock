package com.jongtix.zerock.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jongtix.zerock.domain.board.*;
import com.jongtix.zerock.dto.request.BoardRequestDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BoardControllerTest {

    protected final String ENCODING = "UTF-8";

    @LocalServerPort
    private int port;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @AfterEach
    void tearDown() {
        replyRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("board_리스트_페이지_호출")
    @Test
    void call_list_page() throws Exception {
        //given
        String url = "http://localhost:" + port + "/board/list";

        //when
        ResultActions resultActions = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(ENCODING)
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("result"));
    }

    @DisplayName("board_등록_페이지_호출")
    @Test
    void call_register_page() throws Exception {
        //given
        String url = "http://localhost:" + port + "/board/register";

        //when
        ResultActions resultActions = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(ENCODING)
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("board_등록")
    @Test
    void registerPost() throws Exception {
        //given
        String title = "title";
        String content = "content";
        String email = "email";

        memberRepository.save(
                Member.builder()
                        .email(email)
                        .build()
        );

        String url = "http://localhost:" + port + "/board/register";

        //when
        ResultActions resultActions = mvc.perform(
                post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(ENCODING)
                .param("title", title)
                .param("content", content)
                .param("writerEmail", email)
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("msg"));
    }

    @DisplayName("board_상세_조회")
    @Test
    void read() throws Exception {
        //given
        String title = "title";
        String content = "content";
        String email = "email";

        Member member = memberRepository.save(
                Member.builder()
                        .email(email)
                        .build()
        );

        Long bno = boardRepository.save(
                Board.builder()
                        .title(title)
                        .content(content)
                        .writer(member)
                        .build()
        ).getBno();

        String url = "http://localhost:" + port + "/board/read";

        //when
        ResultActions resultActions = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(ENCODING)
                .param("bno", String.valueOf(bno))
                .content(new ObjectMapper().writeValueAsString(PageRequestDto.builder().build()))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dto"));
    }

    @DisplayName("board_수정_페이지")
    @Test
    void call_modify_page() throws Exception {
        //given
        String title = "title";
        String content = "content";
        String email = "email";

        Member member = memberRepository.save(
                Member.builder()
                        .email(email)
                        .build()
        );

        Long bno = boardRepository.save(
                Board.builder()
                        .title(title)
                        .content(content)
                        .writer(member)
                        .build()
        ).getBno();

        String url = "http://localhost:" + port + "/board/read";

        //when
        ResultActions resultActions = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(ENCODING)
                .param("bno", String.valueOf(bno))
                .content(new ObjectMapper().writeValueAsString(PageRequestDto.builder().build()))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dto"));
    }

    @DisplayName("board_삭제_기능")
    @Test
    void remove() throws Exception {
        //given
        String title = "title";
        String content = "content";
        String email = "email";

        Member member = memberRepository.save(
                Member.builder()
                        .email(email)
                        .build()
        );

        Board board = boardRepository.save(
                Board.builder()
                        .title(title)
                        .content(content)
                        .writer(member)
                        .build()
        );

        String text = "text";
        String replyer = "replyer";

        replyRepository.save(
                Reply.builder()
                        .text(text)
                        .replyer(replyer)
                        .board(board)
                        .build()
        );

        Long bno = board.getBno();

        String url = "http://localhost:" + port + "/board/remove";

        //when
        ResultActions resultActions = mvc.perform(
                delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(ENCODING)
                .param("bno", String.valueOf(bno))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/board/list"));
    }

    @DisplayName("board_수정_기능")
    @Test
    void modify() throws Exception {
        //given
        String title = "title";
        String content = "content";
        String email = "email";

        Member member = memberRepository.save(
                Member.builder()
                        .email(email)
                        .build()
        );

        Long bno = boardRepository.save(
                Board.builder()
                        .title(title)
                        .content(content)
                        .writer(member)
                        .build()
        ).getBno();

        String expectedTitle = "expectedTitle";
        String expectedContent = "expectedContent";

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("bno", String.valueOf(bno));
        params.add("title", expectedTitle);
        params.add("content", expectedContent);
        params.add("writer", "unexpectedWriter");

        String url = "http://localhost:" + port + "/board/modify";

        //when
        ResultActions resultActions = mvc.perform(
                patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(ENCODING)
                .params(params)
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeExists("bno"))
                .andExpect(model().attributeExists("page"))
                .andExpect(view().name("redirect:/board/read"));
    }

}