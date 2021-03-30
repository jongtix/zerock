package com.jongtix.zerock.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {

    private Long reviewNum;

    private Long mno;

    private Long mid;

    private int grade;

    private String text;

}
