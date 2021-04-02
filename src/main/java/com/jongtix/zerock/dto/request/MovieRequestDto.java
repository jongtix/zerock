package com.jongtix.zerock.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequestDto {

    private Long mno;

    private String title;

    @Builder.Default
    private List<MovieImageRequestDto> movieImageRequestDtoList = new ArrayList<>();

}
