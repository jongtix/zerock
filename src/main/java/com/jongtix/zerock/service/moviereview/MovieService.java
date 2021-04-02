package com.jongtix.zerock.service.moviereview;

import com.jongtix.zerock.domain.moviereview.Movie;
import com.jongtix.zerock.domain.moviereview.MovieImage;
import com.jongtix.zerock.dto.request.MovieRequestDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.dto.response.MovieImageResponseDto;
import com.jongtix.zerock.dto.response.MovieResponseDto;
import com.jongtix.zerock.dto.response.PageResponseDto;

import java.util.*;
import java.util.stream.Collectors;

public interface MovieService {

    Long register(MovieRequestDto requestDto);

    PageResponseDto<MovieResponseDto, Object[]> getList(PageRequestDto requestDto);

    MovieResponseDto getMovie(Long mno);

    void removeMovie(Long mno);

    Long modify(MovieRequestDto movieRequestDto);

    default Map<String, Object> dtoToEntity(MovieRequestDto dto) {

        Map<String, Object> result = new HashMap<>();

        Movie movie = Movie.builder()
                .title(dto.getTitle())
                .build();

        result.put("movie", movie);

//        //AS-IS
//        List<MovieImage> movieImageList = dto.getMovieImageRequestDtoList().stream().map(
//                imageDto -> {
//                    return MovieImage.builder()
//                            .uuid(imageDto.getUuid())
//                            .imgName(imageDto.getImgName())
//                            .path(imageDto.getPath())
//                            .build();
//                }
//        ).collect(Collectors.toList());

        //TO-BE
        List<MovieImage> movieImageList =
                Optional.ofNullable(dto.getMovieImageRequestDtoList()).orElseGet(ArrayList::new)
                        .stream()
                        .map(
                                imageDto -> {
                                    return MovieImage.builder()
                                            .uuid(imageDto.getUuid())
                                            .imgName(imageDto.getImgName())
                                            .path(imageDto.getPath())
                                            .movie(movie)
                                            .build();
                                }
                        ).collect(Collectors.toList());

        result.put("imgList", movieImageList);

        return result;
    }

    default MovieResponseDto entityToDto(Movie movie, List<MovieImage> movieImageList, Double avg, Long reviewCnt) {
        return MovieResponseDto.builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .movieImageResponseDtoList(
//                        //AS-IS
//                        movieImageList.stream().map(
//                                movieImage -> {
//                                    return MovieImageResponseDto.builder()
//                                            .uuid(movieImage.getUuid())
//                                            .imgName(movieImage.getImgName())
//                                            .path(movieImage.getPath())
//                                            .build();
//                                }
//                        ).collect(Collectors.toList())

                        //TO-BE
                        Optional.ofNullable(movieImageList).orElseGet(ArrayList::new)
                                .stream()
                                .map(
                                        movieImage -> {
                                            return MovieImageResponseDto.builder()
                                                    .uuid(movieImage.getUuid())
                                                    .imgName(movieImage.getImgName())
                                                    .path(movieImage.getPath())
                                                    .build();
                                        }
                                ).collect(Collectors.toList())
                )
                .avg(avg)
                .reviewCnt(reviewCnt.intValue())
                .regDate(movie.getRegDate())
                .modDate(movie.getModDate())
                .build();
    }

}
