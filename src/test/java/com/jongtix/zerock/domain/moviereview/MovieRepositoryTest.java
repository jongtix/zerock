package com.jongtix.zerock.domain.moviereview;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieMemberRepository memberRepository;

    @Autowired
    private MovieImageRepository movieImageRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAll();
        memberRepository.deleteAll();
        movieImageRepository.deleteAll();
        movieRepository.deleteAll();
    }

    @DisplayName("영화_저장_테스트")
    @Test
    void insert_movie() {
        //given
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);
        String title = "title";

        //when
        Long mno = movieRepository.save(
                Movie.builder()
                        .title(title)
                        .build()
        ).getMno();

        //then
        Movie movie = movieRepository.findById(mno).orElseThrow(() -> new IllegalArgumentException("해당하는 영화가 없습니다. mno: " + mno));
        assertThat(movie.getTitle()).isEqualTo(title);
        assertThat(movie.getRegDate()).isAfter(now);
        assertThat(movie.getModDate()).isAfter(now);
    }

    @DisplayName("영화_목록_테스트")
    @Test
    void test_list_page() {
        //given
        IntStream.rangeClosed(1, 20).forEach(
                i -> {
                    Movie movie = movieRepository.save(
                            Movie.builder()
                                    .title("title" + i)
                                    .build()
                    );

                    movieImageRepository.save(
                            MovieImage.builder()
                                    .uuid(UUID.randomUUID().toString())
                                    .imgName("imgName" + i)
                                    .path("path" + i)
                                    .movie(movie)
                                    .build()
                    );

                    reviewRepository.save(
                            Review.builder()
                                    .movie(movie)
                                    .grade(new Random().nextInt(6))
                                    .text("text" + i)
                                    .build()
                    );
                }
        );

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));

        //when
        Page<Object[]> result = movieRepository.getListPage(pageRequest);

        //then
        assertThat(result.getContent().get(0)[3]).isEqualTo(1L);
        assertThat(((Movie) result.getContent().get(0)[0]).getTitle()).isEqualTo("title20");
        assertThat(((MovieImage) result.getContent().get(0)[1]).getPath()).isEqualTo("path20");
        assertThat(((MovieImage) result.getContent().get(0)[1]).getImgName()).isEqualTo("imgName20");
        assertThat(((Movie) result.getContent().get(9)[0]).getTitle()).isEqualTo("title11");
        assertThat(((MovieImage) result.getContent().get(9)[1]).getPath()).isEqualTo("path11");
        assertThat(((MovieImage) result.getContent().get(9)[1]).getImgName()).isEqualTo("imgName11");
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getTotalElements()).isEqualTo(20);
        assertThat(result.getNumberOfElements()).isEqualTo(10);
        assertThat(result.getSize()).isEqualTo(10);
    }

    @DisplayName("특정_영화_조회")
    @Test
    void getMovieWithAll() {
        //given
        String title = "title";

        Movie movie = movieRepository.save(
                Movie.builder()
                        .title(title)
                        .build()
        );

        String uuid1 = UUID.randomUUID().toString();
        String imgName1 = "imgName1";
        String path1 = "path1";

        movieImageRepository.save(
                MovieImage.builder()
                        .uuid(uuid1)
                        .imgName(imgName1)
                        .path(path1)
                        .movie(movie)
                        .build()
        );

        String uuid2 = UUID.randomUUID().toString();
        String imgName2 = "imgName2";
        String path2 = "path2";

        movieImageRepository.save(
                MovieImage.builder()
                        .uuid(uuid2)
                        .imgName(imgName2)
                        .path(path2)
                        .movie(movie)
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

        int grade = new Random().nextInt(6);
        String text = "text";

        reviewRepository.save(
                Review.builder()
                        .grade(grade)
                        .text(text)
                        .movie(movie)
                        .movieMember(member)
                        .build()
        );

        Long mno = movie.getMno();

        //when
        List<Object[]> result = movieRepository.getMovieWithAll(mno);

        //then
        assertThat(result.get(0)[3]).isEqualTo(1L); //리뷰 개수
        assertThat(((Movie) result.get(0)[0]).getTitle()).isEqualTo(title);
        assertThat(((MovieImage) result.get(0)[1]).getUuid()).isEqualTo(uuid1);
        assertThat(((MovieImage) result.get(0)[1]).getImgName()).isEqualTo(imgName1);
        assertThat(((MovieImage) result.get(0)[1]).getPath()).isEqualTo(path1);
        assertThat(((Movie) result.get(1)[0]).getTitle()).isEqualTo(title);
        assertThat(((MovieImage) result.get(1)[1]).getUuid()).isEqualTo(uuid2);
        assertThat(((MovieImage) result.get(1)[1]).getImgName()).isEqualTo(imgName2);
        assertThat(((MovieImage) result.get(1)[1]).getPath()).isEqualTo(path2);
    }

}