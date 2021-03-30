package com.jongtix.zerock.service.moviereview;

import com.jongtix.zerock.domain.board.ReplyRepository;
import com.jongtix.zerock.domain.moviereview.*;
import com.jongtix.zerock.dto.request.MovieImageRequestDto;
import com.jongtix.zerock.dto.request.MovieRequestDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.dto.response.MovieResponseDto;
import com.jongtix.zerock.dto.response.PageResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.testcontainers.containers.GenericContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MovieServiceTest {

    @Autowired
    private MovieService service;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository imageRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAll();
        imageRepository.deleteAll();
        movieRepository.deleteAll();
    }

    @DisplayName("영화_리스트_조회")
    @Test
    void getList() {
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
        imageRepository.save(
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
        imageRepository.save(
                MovieImage.builder()
                        .uuid(uuid2)
                        .imgName(imgName2)
                        .path(path2)
                        .movie(movie)
                        .build()
        );

        IntStream.rangeClosed(1, 20).forEach(
                i -> {
                    reviewRepository.save(
                            Review.builder()
                                    .movie(movie)
                                    .grade(new Random().nextInt(6))
                                    .text("text" + i)
                                    .build()
                    );
                }
        );

        //when
        PageResponseDto<MovieResponseDto, Object[]> responseDto = service.getList(new PageRequestDto());

        //then
        assertThat(responseDto.getDtoList().size()).isEqualTo(1);
        assertThat(responseDto.getDtoList().get(0).getTitle()).isEqualTo(title);
        assertThat(responseDto.getDtoList().get(0).getMovieImageResponseDtoList().get(0).getUuid()).isEqualTo(uuid1);
        assertThat(responseDto.getDtoList().get(0).getMovieImageResponseDtoList().get(0).getImgName()).isEqualTo(imgName1);
        assertThat(responseDto.getDtoList().get(0).getMovieImageResponseDtoList().get(0).getPath()).isEqualTo(path1);
        assertThat(responseDto.getDtoList().get(0).getAvg()).isGreaterThan(0).isLessThan(5);
        assertThat(responseDto.getDtoList().get(0).getReviewCnt()).isEqualTo(20);
    }

    @DisplayName("영화_등록_테스트")
    @Test
    void register() {
        //given
        String title = "title";

        List<MovieImageRequestDto> movieImageRequestDtoList = new ArrayList<>();
        String uuid1 = UUID.randomUUID().toString();
        String imgName1 = "imgName1";
        String path1 = "path1";

        movieImageRequestDtoList.add(MovieImageRequestDto.builder()
                .uuid(uuid1)
                .imgName(imgName1)
                .path(path1)
                .build());

        String uuid2 = UUID.randomUUID().toString();
        String imgName2 = "imgName2";
        String path2 = "path2";

        movieImageRequestDtoList.add(MovieImageRequestDto.builder()
                .uuid(uuid2)
                .imgName(imgName2)
                .path(path2)
                .build());

        MovieRequestDto dto = MovieRequestDto.builder()
                .title(title)
                .movieImageRequestDtoList(movieImageRequestDtoList)
                .build();

        //when
        Long resultMno = service.register(dto);
        Movie movie = movieRepository.findById(resultMno).orElseThrow(() -> new IllegalArgumentException("해당하는 영화가 없습니다. mno: " + resultMno));
        List<MovieImage> imageList = imageRepository.findAll();

        //then
        assertThat(movie.getTitle()).isEqualTo(title);
        assertThat(imageList.size()).isEqualTo(2);
        assertThat(imageList.get(0).getUuid()).isEqualTo(uuid1);
        assertThat(imageList.get(0).getImgName()).isEqualTo(imgName1);
        assertThat(imageList.get(0).getPath()).isEqualTo(path1);
        assertThat(imageList.get(1).getUuid()).isEqualTo(uuid2);
        assertThat(imageList.get(1).getImgName()).isEqualTo(imgName2);
        assertThat(imageList.get(1).getPath()).isEqualTo(path2);
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

        List<MovieImage> movieImageList = new ArrayList<>();
        String uuid1 = UUID.randomUUID().toString();
        String imgName1 = "imgName1";
        String path1 = "path1";
        movieImageList.add(
                MovieImage.builder()
                        .uuid(uuid1)
                        .imgName(imgName1)
                        .path(path1)
                        .build()
        );
        String uuid2 = UUID.randomUUID().toString();
        String imgName2 = "imgName2";
        String path2 = "path2";
        movieImageList.add(
                MovieImage.builder()
                        .uuid(uuid2)
                        .imgName(imgName2)
                        .path(path2)
                        .build()
        );

        Double avg = 4.5;
        Long reviewCnt = 100L;

        //when
        MovieResponseDto responseDto = service.entityToDto(movie, movieImageList, avg, reviewCnt);

        //then
        assertThat(responseDto.getMno()).isEqualTo(movie.getMno());
        assertThat(responseDto.getTitle()).isEqualTo(title);
        assertThat(responseDto.getMovieImageResponseDtoList().size()).isEqualTo(2);
        assertThat(responseDto.getMovieImageResponseDtoList().get(0).getUuid()).isEqualTo(uuid1);
        assertThat(responseDto.getMovieImageResponseDtoList().get(0).getImgName()).isEqualTo(imgName1);
        assertThat(responseDto.getMovieImageResponseDtoList().get(0).getPath()).isEqualTo(path1);
        assertThat(responseDto.getMovieImageResponseDtoList().get(1).getUuid()).isEqualTo(uuid2);
        assertThat(responseDto.getMovieImageResponseDtoList().get(1).getImgName()).isEqualTo(imgName2);
        assertThat(responseDto.getMovieImageResponseDtoList().get(1).getPath()).isEqualTo(path2);
        assertThat(responseDto.getAvg()).isEqualTo(avg);
        assertThat(responseDto.getReviewCnt()).isEqualTo(reviewCnt.intValue());
        assertThat(responseDto.getRegDate()).isAfter(now);
        assertThat(responseDto.getModDate()).isAfter(now);
    }

    @DisplayName("Entity_DTO_변환_테스트_imageList의_size_0일_때")
    @Test
    void entityToDtoImageListEmpty() {
        //given
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);
        String title = "title";
        Movie movie = movieRepository.save(
                Movie.builder()
                        .title(title)
                        .build()
        );

        List<MovieImage> movieImageList = new ArrayList<>();

        Double avg = 4.5;
        Long reviewCnt = 100L;

        //when
        MovieResponseDto responseDto = service.entityToDto(movie, movieImageList, avg, reviewCnt);

        //then
        assertThat(responseDto.getMno()).isEqualTo(movie.getMno());
        assertThat(responseDto.getTitle()).isEqualTo(title);
        assertThat(responseDto.getMovieImageResponseDtoList().size()).isEqualTo(0);
        assertThat(responseDto.getAvg()).isEqualTo(avg);
        assertThat(responseDto.getReviewCnt()).isEqualTo(reviewCnt.intValue());
        assertThat(responseDto.getRegDate()).isAfter(now);
        assertThat(responseDto.getModDate()).isAfter(now);
    }

    @DisplayName("Entity_DTO_변환_테스트_imageList_Null일_때")
    @Test
    void entityToDtoImageListNull() {
        //given
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);
        String title = "title";
        Movie movie = movieRepository.save(
                Movie.builder()
                        .title(title)
                        .build()
        );

        Double avg = 4.5;
        Long reviewCnt = 100L;

        //when
        MovieResponseDto responseDto = service.entityToDto(movie, null, avg, reviewCnt);

        //then
        assertThat(responseDto.getMno()).isEqualTo(movie.getMno());
        assertThat(responseDto.getTitle()).isEqualTo(title);
        assertThat(responseDto.getMovieImageResponseDtoList().size()).isEqualTo(0);
        assertThat(responseDto.getAvg()).isEqualTo(avg);
        assertThat(responseDto.getReviewCnt()).isEqualTo(reviewCnt.intValue());
        assertThat(responseDto.getRegDate()).isAfter(now);
        assertThat(responseDto.getModDate()).isAfter(now);
    }

    @DisplayName("DTO_Entity_변환_테스트")
    @Test
    void dtoToEntity() {
        //given
        String title = "title";

        List<MovieImageRequestDto> movieImageRequestDtoList = new ArrayList<>();
        String uuid = UUID.randomUUID().toString();
        String imgName = "imgName";
        String path = "path";

        movieImageRequestDtoList.add(MovieImageRequestDto.builder()
                .uuid(uuid)
                .imgName(imgName)
                .path(path)
                .build());

        MovieRequestDto dto = MovieRequestDto.builder()
                .title(title)
                .movieImageRequestDtoList(movieImageRequestDtoList)
                .build();

        //when
        Map<String, Object> result = service.dtoToEntity(dto);

        //then
        assertThat(result.get("movie")).isNotNull();
        assertThat(((Movie) result.get("movie")).getTitle()).isEqualTo(title);
        assertThat(result.get("imgList")).isNotNull();
        assertThat(((MovieImage) ((List) result.get("imgList")).get(0)).getUuid()).isEqualTo(uuid);
        assertThat(((MovieImage) ((List) result.get("imgList")).get(0)).getImgName()).isEqualTo(imgName);
        assertThat(((MovieImage) ((List) result.get("imgList")).get(0)).getPath()).isEqualTo(path);
    }

    @DisplayName("DTO_Entity_변환_테스트_imgList_NULL일_때")
    @Test
    void dtoToEntityImgListNull() {
        //given
        String title = "title";

        MovieRequestDto dto = MovieRequestDto.builder()
                .title(title)
                .movieImageRequestDtoList(null)
                .build();

        //when
        Map<String, Object> result = service.dtoToEntity(dto);

        //then
        assertThat(result.get("movie")).isNotNull();
        assertThat(((Movie) result.get("movie")).getTitle()).isEqualTo(title);
        assertThat(result.get("imgList")).isNotNull();
    }

    @DisplayName("DTO_Entity_변환_테스트_imgList_size_0일_때")
    @Test
    void dtoToEntityImgListEmpty() {
        //given
        String title = "title";
        List<MovieImageRequestDto> imgList = new ArrayList<>();

        MovieRequestDto dto = MovieRequestDto.builder()
                .title(title)
                .movieImageRequestDtoList(imgList)
                .build();

        //when
        Map<String, Object> result = service.dtoToEntity(dto);

        //then
        assertThat(result.get("movie")).isNotNull();
        assertThat(((Movie) result.get("movie")).getTitle()).isEqualTo(title);
        assertThat(result.get("imgList")).isNotNull();
    }

}