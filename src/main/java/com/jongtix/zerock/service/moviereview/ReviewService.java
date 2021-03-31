package com.jongtix.zerock.service.moviereview;

import com.jongtix.zerock.domain.moviereview.Movie;
import com.jongtix.zerock.domain.moviereview.MovieMember;
import com.jongtix.zerock.domain.moviereview.Review;
import com.jongtix.zerock.dto.request.ReviewRequestDto;
import com.jongtix.zerock.dto.response.ReviewResponseDto;

import java.util.List;

public interface ReviewService {

    List<ReviewResponseDto> getListOfMovie(Long mno);

    Long register(ReviewRequestDto requestDto);

    void modify(ReviewRequestDto requestDto);

    void remove(Long reviewnum);

    default Review dtoToEntity(ReviewRequestDto dto) {
        return Review.builder()
                .reviewnum(dto.getReviewnum())
                .text(dto.getText())
                .grade(dto.getGrade())
                .movie(
                        Movie.builder()
                                .mno(dto.getMno())
                                .build()
                )
                .movieMember(
                        MovieMember.builder()
                                .mid(dto.getMid())
                                .build()
                )
                .build();
    }

    default ReviewResponseDto entityToDto(Review entity) {
        return ReviewResponseDto.builder()
                .reviewnum(entity.getReviewnum())
                .mno(entity.getMovie().getMno())
                .mid(entity.getMovieMember().getMid())
                .nickname(entity.getMovieMember().getNickname())
                .email(entity.getMovieMember().getEmail())
                .grade(entity.getGrade())
                .text(entity.getText())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

}
