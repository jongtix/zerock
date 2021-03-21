package com.jongtix.zerock.domain.moviereview;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MovieImageRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository movieImageRepository;

    @AfterEach
    void tearDown() {
        movieImageRepository.deleteAll();
        movieRepository.deleteAll();
    }

    @DisplayName("영화_이미지_저장_테스트")
    @Test
    void insert_movie_image() {
        //given
        String title = "title";
        Movie movie = movieRepository.save(
                Movie.builder()
                        .title(title)
                        .build()
        );

        String uuid = UUID.randomUUID().toString();
        String imgName = "imgName.jpg";

        //when
        MovieImage movieImage = movieImageRepository.save(
                MovieImage.builder()
                        .uuid(uuid)
                        .imgName(imgName)
                        .movie(movie)
                        .build()
        );

        //then
        assertThat(movieImage.getUuid()).isEqualTo(uuid);
        assertThat(movieImage.getImgName()).isEqualTo(imgName);
        assertThat(movieImage.getMovie().getTitle()).isEqualTo(title);
    }
}