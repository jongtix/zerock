package com.jongtix.zerock.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jongtix.zerock.domain.moviereview.*;
import com.jongtix.zerock.dto.request.ReviewRequestDto;
import com.jongtix.zerock.utils.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieMemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

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

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAll();
        memberRepository.deleteAll();
        movieRepository.deleteAll();
    }

    @DisplayName("리뷰_리스트_가져오기_테스트")
    @Test
    void getList() throws Exception {
        //given
        Movie movie = movieRepository.save(Movie.builder().build());

        MovieMember member1 = memberRepository.save(MovieMember.builder().build());
        String text1 = "text1";
        int grade1 = new Random().nextInt(6);
        Long reviewNum1 = reviewRepository.save(
                Review.builder()
                        .text(text1)
                        .grade(grade1)
                        .movie(movie)
                        .movieMember(member1)
                        .build()
        ).getReviewnum();

        MovieMember member2 = memberRepository.save(MovieMember.builder().build());
        String text2 = "text2";
        int grade2 = new Random().nextInt(6);
        Long reviewNum2 = reviewRepository.save(
                Review.builder()
                        .text(text2)
                        .grade(grade2)
                        .movie(movie)
                        .movieMember(member2)
                        .build()
        ).getReviewnum();

        String url = "http://localhost:" + port + "/reviews/{mno}/all";

        //when
        ResultActions resultActions = mvc.perform(
                get(url, movie.getMno())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(Constants.DEFAULT_ENCODING)
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].mno").value(movie.getMno()))
                .andExpect(jsonPath("$.[0].mid").value(member1.getMid()))
                .andExpect(jsonPath("$.[0].reviewNum").value(reviewNum1))
                .andExpect(jsonPath("$.[0].grade").value(grade1))
                .andExpect(jsonPath("$.[0].text").value(text1))
                .andExpect(jsonPath("$.[1].mno").value(movie.getMno()))
                .andExpect(jsonPath("$.[1].mid").value(member2.getMid()))
                .andExpect(jsonPath("$.[1].reviewNum").value(reviewNum2))
                .andExpect(jsonPath("$.[1].grade").value(grade2))
                .andExpect(jsonPath("$.[1].text").value(text2));
    }

    @DisplayName("리뷰_등록_테스트")
    @Test
    void addReview() throws Exception {
        //given
        Long mno = movieRepository.save(Movie.builder().build()).getMno();
        Long mid = memberRepository.save(MovieMember.builder().build()).getMid();
        int grade = new Random().nextInt(6);
        String text = "text";

        ReviewRequestDto requestDto = ReviewRequestDto.builder()
                .mno(mno)
                .mid(mid)
                .grade(grade)
                .text(text)
                .build();

        String url = "http://localhost:" + port + "/reviews/{mno}";

        //when
        ResultActions resultActions = mvc.perform(
                post(url, mno)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(Constants.DEFAULT_ENCODING)
                .content(new ObjectMapper().writeValueAsString(requestDto))
        );

        //then
        resultActions
                .andExpect(status().isCreated());
    }

    @DisplayName("리뷰_수정_테스트")
    @Test
    void modifyReview() throws Exception {
        //given
        Movie movie = movieRepository.save(Movie.builder().build());
        MovieMember member = memberRepository.save(MovieMember.builder().build());

        String text = "text";
        int grade = new Random().nextInt(6);
        Long reviewNum = reviewRepository.save(
                Review.builder()
                        .text(text)
                        .grade(grade)
                        .movie(movie)
                        .movieMember(member)
                        .build()
        ).getReviewnum();

        String expectedText = "expectedText";
        int expectedGrade = new Random().nextInt(6);
        ReviewRequestDto requestDto = ReviewRequestDto.builder()
                .reviewNum(reviewNum)
                .grade(expectedGrade)
                .text(expectedText)
                .build();

        String url = "http://localhost:" + port + "/reviews/{mno}/{reviewnum}";

        //when
        ResultActions resultActions = mvc.perform(
                put(url, movie.getMno(), reviewNum)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(Constants.DEFAULT_ENCODING)
                .content(new ObjectMapper().writeValueAsString(requestDto))
        );

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(reviewNum));
    }

    @DisplayName("리뷰_삭제_테스트")
    @Test
    void removeReview() throws Exception {
        //given
        Movie movie = movieRepository.save(Movie.builder().build());
        String text = "text";
        int grade = new Random().nextInt(6);

        Long reviewNum = reviewRepository.save(
                Review.builder()
                        .text(text)
                        .grade(grade)
                        .movie(movie)
                        .movieMember(
                                memberRepository.save(
                                        MovieMember.builder().build()
                                )
                        )
                        .build()
        ).getReviewnum();

        String url = "http://localhost:" + port + "/reviews/{mno}/{reviewnum}";

        //when
        ResultActions resultActions = mvc.perform(
                delete(url, movie.getMno(), reviewNum)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(Constants.DEFAULT_ENCODING)
        );

        //then
        resultActions
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").value(reviewNum));
    }
}