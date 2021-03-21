package com.jongtix.zerock.domain.moviereview;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ReviewRepositoryTest {

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

    @DisplayName("영화_리뷰_저장_테스트")
    @Test
    void insert_movie_review() {
        //given
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);
        String title = "title";
        Movie movie = movieRepository.save(
                Movie.builder()
                        .title(title)
                        .build()
        );

        String email = "email";
        String password = "password";
        String nickname = "nickname";
        MovieMember member = memberRepository.save(
                MovieMember.builder()
                        .email(email)
                        .password(password)
                        .nickname(nickname)
                        .build()
        );

        int grade = 5;
        String text = "text";

        //when
        Review review = reviewRepository.save(
                Review.builder()
                        .movie(movie)
                        .movieMember(member)
                        .grade(grade)
                        .text(text)
                        .build()
        );

        //then
        assertThat(review.getMovie().getTitle()).isEqualTo(title);
        assertThat(review.getMovieMember().getEmail()).isEqualTo(email);
        assertThat(review.getMovieMember().getPassword()).isEqualTo(password);
        assertThat(review.getMovieMember().getNickname()).isEqualTo(nickname);
        assertThat(review.getGrade()).isEqualTo(grade);
        assertThat(review.getText()).isEqualTo(text);
        assertThat(review.getRegDate()).isAfter(now);
        assertThat(review.getModDate()).isAfter(now);
    }

    @DisplayName("특정_영화에_대한_모든_리뷰")
//    @Transactional  //no Session error 발생
                    //원인: Review Entity의 Member에 대한 Fetch 방식이 LAZY이기 때문에 한 번에 Review 객체와 Member 객체를 조회할 수 없기 때문에 발생
                    //@Transactional을 적용해도 각 Review 객체의 getMember().getEmail()을 처리할 때마다 Member 객체를 로딩해야하는 문제가 있음
                    //해결 방법1: Query를 이용해서 조인 처리(join fetch 쿼리문 작성) -> Inner Join으로 처리 됨
                    //해결 방법2: Repository의 특정 Method에 @EntityGraph를 이용해서 Review 객체를 가져올 때 Member 객체를 로딩 -> Outer Join으로 처리 됨
    @Test
    void findByMovie() {
        //given
        String title = "title";

        Movie movie = movieRepository.save(
                Movie.builder()
                        .title(title)
                        .build()
        );

        String email1 = "email1";
        String password1 = "password1";
        String nickname1 = "nickname1";
        int grade1 = new Random().nextInt(6);
        String text1 = "text1";

        reviewRepository.save(
                Review.builder()
                        .grade(grade1)
                        .text(text1)
                        .movie(movie)
                        .movieMember(
                                memberRepository.save(MovieMember.builder()
                                        .email(email1)
                                        .password(password1)
                                        .nickname(nickname1)
                                        .build())
                        )
                        .build()
        );

        String email2 = "email2";
        String password2 = "password2";
        String nickname2 = "nickname2";
        int grade2 = new Random().nextInt(6);
        String text2 = "text2";

        reviewRepository.save(
                Review.builder()
                        .grade(grade2)
                        .text(text2)
                        .movie(movie)
                        .movieMember(memberRepository.save(
                                MovieMember.builder()
                                        .email(email2)
                                        .password(password2)
                                        .nickname(nickname2)
                                        .build())
                        )
                        .build()
        );

        //when
        List<Review> result = reviewRepository.findByMovie(movie);

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getGrade()).isEqualTo(grade1);
        assertThat(result.get(0).getText()).isEqualTo(text1);
        assertThat(result.get(0).getMovieMember().getEmail()).isEqualTo(email1);
        assertThat(result.get(0).getMovieMember().getPassword()).isEqualTo(password1);
        assertThat(result.get(0).getMovieMember().getNickname()).isEqualTo(nickname1);
        assertThat(result.get(1).getGrade()).isEqualTo(grade2);
        assertThat(result.get(1).getText()).isEqualTo(text2);
        assertThat(result.get(1).getMovieMember().getEmail()).isEqualTo(email2);
        assertThat(result.get(1).getMovieMember().getPassword()).isEqualTo(password2);
        assertThat(result.get(1).getMovieMember().getNickname()).isEqualTo(nickname2);
    }

    /**
     * 삭제 처리의 경우
     * 1) FK를 갖는 Review 쪽을 먼저 삭제해야 함
     * 2) 트랜잭션 관련 처리(@Commit & @Transactional)를 해야 함
     */
    @DisplayName("Member를_삭제했을_경우_Review_삭제_테스트")
    @Commit
    @Transactional
    @Test
    void deleteByMember() {
        //given
        String email = "email";
        String password = "password";
        String nickname = "nickname";

        MovieMember member = memberRepository.save(
                MovieMember.builder()
                        .email(email)
                        .password(password)
                        .nickname(nickname)
                        .build()
        );

        int grade1 = new Random().nextInt(6);
        String text1 = "text1";

        Long reviewNum1 = reviewRepository.save(
                Review.builder()
                        .grade(grade1)
                        .text(text1)
                        .movieMember(member)
                        .build()
        ).getReviewnum();

        int grade2 = new Random().nextInt(6);
        String text2 = "text2";

        Long reviewNum2 = reviewRepository.save(
        Review.builder()
                .grade(grade2)
                .text(text2)
                        .movieMember(member)
                        .build()
        ).getReviewnum();

        //when
        reviewRepository.deleteByMovieMember(member);
        memberRepository.deleteById(member.getMid());

        //then
        assertThrows(NoSuchElementException.class, () -> {
                    reviewRepository.findById(reviewNum1).orElseThrow(() -> new NoSuchElementException("해당하는 리뷰가 없습니다. reviewNum: " + reviewNum1));
                    reviewRepository.findById(reviewNum2).orElseThrow(() -> new NoSuchElementException("해당하는 리뷰가 없습니다. reviewNum: " + reviewNum2));
                }
        );
        assertThrows(NoSuchElementException.class, () ->
                memberRepository.findById(member.getMid()).orElseThrow(() -> new NoSuchElementException("해당하는 멤버가 없습니다. mid: " + member.getMid()))
        );
    }

}