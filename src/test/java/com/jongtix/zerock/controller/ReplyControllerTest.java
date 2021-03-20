package com.jongtix.zerock.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jongtix.zerock.domain.board.Board;
import com.jongtix.zerock.domain.board.BoardRepository;
import com.jongtix.zerock.domain.board.Reply;
import com.jongtix.zerock.domain.board.ReplyRepository;
import com.jongtix.zerock.dto.request.ReplyRequestDto;
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

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReplyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @DisplayName("댓글_조회_테스트")
    @Test
    void getListByBoard() throws Exception {
        //given
        Board board = boardRepository.save(
                Board.builder()
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

        String url = "http://localhost:" + port + "/replies/board/{bno}";

        //when
        ResultActions resultActions = mvc.perform(
                get(url, board.getBno())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value(text))
                .andExpect(jsonPath("$[0].replyer").value(replyer));
    }

    @DisplayName("댓글_등록_테스트")
    @Test
    void register() throws Exception {
        //given
        Long bno = boardRepository.save(
                Board.builder()
                        .build()
        ).getBno();

        String text = "text";
        String replyer = "replyer";

        ReplyRequestDto requestDto = ReplyRequestDto.builder()
                .text(text)
                .replyer(replyer)
                .bno(bno)
                .build();

        String url = "http://localhost:" + port + "/replies";

        //when
        ResultActions resultActions = mvc.perform(
                post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(new ObjectMapper().writeValueAsString(requestDto))
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("댓글_삭제_테스트")
    @Test
    void remove() throws Exception {
        //given
        Board board = boardRepository.save(
                Board.builder()
                        .build()
        );

        String text = "text";
        String replyer = "replyer";
        Long rno = replyRepository.save(
                Reply.builder()
                        .text(text)
                        .replyer(replyer)
                        .board(board)
                        .build()
        ).getRno();

        String url = "http://localhost:" + port + "/replies/{rno}";

        //when
        ResultActions resultActions = mvc.perform(
                delete(url, rno)
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("success"));
    }

    @DisplayName("댓글_수정_테스트")
    @Test
    void modify() throws Exception {
        //given
        Board board = boardRepository.save(
                Board.builder()
                        .build()
        );

        String text = "text";
        String replyer = "replyer";
        Long rno = replyRepository.save(
                Reply.builder()
                        .text(text)
                        .replyer(replyer)
                        .board(board)
                        .build()
        ).getRno();

        String expectedText = "expectedText";
        String expectedReplyer = "expectedReplyer";

        ReplyRequestDto requestDto = ReplyRequestDto.builder()
                .rno(rno)
                .text(expectedText)
                .replyer(expectedReplyer)
                .bno(board.getBno())
                .build();

        String url = "http://localhost:" + port + "/replies/{rno}";

        //when
        ResultActions resultActions = mvc.perform(
                put(url, rno)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(new ObjectMapper().writeValueAsString(requestDto))
        );

        Reply reply = replyRepository.findById(rno).orElseThrow(() -> new IllegalArgumentException("해당하는 댓글이 없습니다. rno: " + rno));

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("success"));
        assertThat(reply.getText()).isEqualTo(expectedText);
        assertThat(reply.getReplyer()).isEqualTo(expectedReplyer);
    }

}