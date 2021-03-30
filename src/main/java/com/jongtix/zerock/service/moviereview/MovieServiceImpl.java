package com.jongtix.zerock.service.moviereview;

import com.jongtix.zerock.domain.moviereview.Movie;
import com.jongtix.zerock.domain.moviereview.MovieImage;
import com.jongtix.zerock.domain.moviereview.MovieImageRepository;
import com.jongtix.zerock.domain.moviereview.MovieRepository;
import com.jongtix.zerock.dto.request.MovieRequestDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.dto.response.MovieResponseDto;
import com.jongtix.zerock.dto.response.PageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    private final MovieImageRepository imageRepository;

    @Override
    public Long register(MovieRequestDto requestDto) {

        Map<String, Object> entityMap = dtoToEntity(requestDto);

        Movie movie = (Movie) entityMap.get("movie");
        Long result = movieRepository.save(movie).getMno();

//        //AS-IS
//        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");
//        movieImageList.forEach(movieImage -> {
//            imageRepository.save(movieImage);
//        });

        //TO-BE
        if (entityMap.get("imgList") instanceof List) {
            for (Object obj : (List<?>) entityMap.get("imgList")) {
                if (obj instanceof MovieImage) {
                    imageRepository.save((MovieImage) obj);
                }
            }
        }

        return result;
    }

    @Override
    public PageResponseDto<MovieResponseDto, Object[]> getList(PageRequestDto requestDto) {

        Pageable pageable = requestDto.getPageable(Sort.by("mno").descending());

        Page<Object[]> result = movieRepository.getListPage(pageable);

        Function<Object[], MovieResponseDto> function = (
                arr -> entityToDto(
                        (Movie) arr[0],
                        (List<MovieImage>) Arrays.asList((MovieImage) arr[1]),
                        (Double) arr[2],
                        (Long) arr[3]
                )
        );

        return new PageResponseDto<>(result, function);
    }

    @Override
    public MovieResponseDto getMovie(Long mno) {

        List<Object[]> result = movieRepository.getMovieWithAll(mno);

        Movie movie = (Movie) result.get(0)[0];

        List<MovieImage> movieImageList = new ArrayList<>();

        result.forEach(
                array -> {
                    movieImageList.add((MovieImage) array[1]);
                }
        );

        Double avg = (Double) result.get(0)[2];

        Long reviewCnt = (Long) result.get(0)[3];

        return entityToDto(movie, movieImageList, avg, reviewCnt);
    }


}
