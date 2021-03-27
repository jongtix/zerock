package com.jongtix.zerock.dto.response;

import com.jongtix.zerock.dto.request.MovieImageRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponseDto {

    private Long mno;

    private String title;

    @Builder.Default
    private List<MovieImageResponseDto> movieImageResponseDtoList = new ArrayList<>();

    private double avg; //영화 평균 평점

    private int reviewCnt;  //리뷰 수

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
