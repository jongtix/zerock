package com.jongtix.zerock.service.moviereview;

import com.jongtix.zerock.domain.moviereview.*;
import com.jongtix.zerock.dto.request.ReviewRequestDto;
import com.jongtix.zerock.dto.response.ReviewResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieMemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAll();
        memberRepository.deleteAll();
        movieRepository.deleteAll();
    }

    @DisplayName("리뷰_삭제_테스트")
    @Test
    void remove() {
        //given
        String text = "text";
        int grade = new Random().nextInt(6);

        Long reviewnum = reviewRepository.save(
                Review.builder()
                        .text(text)
                        .grade(grade)
                        .movie(
                                movieRepository.save(
                                        Movie.builder().build()
                                )
                        )
                        .movieMember(
                                memberRepository.save(
                                        MovieMember.builder().build()
                                )
                        )
                        .build()
        ).getReviewnum();

        //when
        reviewService.remove(reviewnum);

        //then
        /**
         * NoSuchElementException: 구성요소가 그 이상 없는 경우 발생
         * IllegalArgumentException: 메소드의 전달 인자값이 잘못될 경우 발생
         * 출처: https://exynoa.tistory.com/125?category=431864
         */
        assertThrows(NoSuchElementException.class, () -> {
            reviewRepository.findById(reviewnum).orElseThrow(() -> new NoSuchElementException("해당하는 리뷰가 없습니다. reviewnum: " + reviewnum));
        });
    }

    @DisplayName("리뷰_수정_테스트")
    @Test
    void modify() {
        //given
        Movie movie = movieRepository.save(Movie.builder().build());
        MovieMember member = memberRepository.save(MovieMember.builder().build());

        String text = "text";
        int grade = new Random().nextInt(6);
        Long reviewnum = reviewRepository.save(
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
                .reviewnum(reviewnum)
                .grade(expectedGrade)
                .text(expectedText)
                .build();

        //when
        reviewService.modify(requestDto);
        Review review = reviewRepository.findById(reviewnum).orElseThrow(() -> new IllegalArgumentException("해당하는 리뷰가 없습니다. reviewnum: " + reviewnum));

        //then
        assertThat(review.getReviewnum()).isEqualTo(reviewnum);
        assertThat(review.getGrade()).isEqualTo(expectedGrade);
        assertThat(review.getText()).isEqualTo(expectedText);
        assertThat(review.getMovie().getMno()).isEqualTo(movie.getMno());
        assertThat(review.getMovieMember().getMid()).isEqualTo(member.getMid());
    }

    @DisplayName("리뷰_등록_테스트")
    @Test
    void register() {
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

        //when
        Long result = reviewService.register(requestDto);
        Review review = reviewRepository.findById(result).orElseThrow(() -> new IllegalArgumentException("해당하는 리뷰가 없습니다. reviewnum: " + result));

        //then
        assertThat(review.getGrade()).isEqualTo(grade);
        assertThat(review.getText()).isEqualTo(text);
        assertThat(review.getMovie().getMno()).isEqualTo(mno);
        assertThat(review.getMovieMember().getMid()).isEqualTo(mid);
    }

    @DisplayName("영화_번호로_리뷰_목록_조회")
    @Test
    void getListOfMovie() {
        //given
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);
        String title = "title";
        Movie movie = movieRepository.save(
                Movie.builder()
                        .title(title)
                        .build()
        );

        String nickname1 = "nickname1";
        String email1 = "email1";
        MovieMember member1 = memberRepository.save(
                MovieMember.builder()
                        .nickname(nickname1)
                        .email(email1)
                        .build()
        );

        String text1 = "text1";
        int grade1 = new Random().nextInt(6);
        reviewRepository.save(
                Review.builder()
                        .text(text1)
                        .grade(grade1)
                        .movie(movie)
                        .movieMember(member1)
                        .build()
        );

        String nickname2 = "nickname2";
        String email2 = "email";
        MovieMember member2 = memberRepository.save(
                MovieMember.builder()
                        .nickname(nickname2)
                        .email(email2)
                        .build()
        );

        String text2 = "text2";
        int grade2 = new Random().nextInt(6);
        reviewRepository.save(
                Review.builder()
                        .text(text2)
                        .grade(grade2)
                        .movie(movie)
                        .movieMember(member2)
                        .build()
        );

        //when
        List<ReviewResponseDto> result = reviewService.getListOfMovie(movie.getMno());

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @DisplayName("DTO_Entity_변환_테스트")
    @Test
    void dtoToEntity() {
        //given
        String text = "text";
        int grade = new Random().nextInt(6);
        Long mno = 1L;
        Long mid = 1L;

        ReviewRequestDto requestDto = ReviewRequestDto.builder()
                .text(text)
                .grade(grade)
                .mno(mno)
                .mid(mid)
                .build();

        //when
        Review review = reviewService.dtoToEntity(requestDto);

        //then
        assertThat(review.getText()).isEqualTo(text);
        assertThat(review.getGrade()).isEqualTo(grade);
        assertThat(review.getMovie().getMno()).isEqualTo(mno);
        assertThat(review.getMovieMember().getMid()).isEqualTo(mid);
    }

    @DisplayName("Entity_DTO_변환_테스트")
    @Test
    void entityToDto() {
        //given
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);
        String title = "title";
        Movie movie = movieRepository.save(
                Movie.builder()
                        .title(title)
                        .build()
        );

        String nickname = "nickname";
        String email = "email";
        MovieMember member = memberRepository.save(
                MovieMember.builder()
                        .nickname(nickname)
                        .email(email)
                        .build()
        );

        String text = "text";
        int grade = new Random().nextInt(6);
        Review review = reviewRepository.save(
                Review.builder()
                        .text(text)
                        .grade(grade)
                        .movie(movie)
                        .movieMember(member)
                        .build()
        );

        //when
        ReviewResponseDto responseDto = reviewService.entityToDto(review);

        //then
        assertThat(responseDto.getReviewnum()).isEqualTo(review.getReviewnum());
        assertThat(responseDto.getMno()).isEqualTo(movie.getMno());
        assertThat(responseDto.getMid()).isEqualTo(member.getMid());
        assertThat(responseDto.getNickname()).isEqualTo(member.getNickname());
        assertThat(responseDto.getEmail()).isEqualTo(member.getEmail());
        assertThat(responseDto.getGrade()).isEqualTo(review.getGrade());
        assertThat(responseDto.getText()).isEqualTo(review.getText());
        assertThat(responseDto.getRegDate()).isAfter(now);
        assertThat(responseDto.getRegDate()).isAfter(now);
    }

}