package com.jongtix.zerock.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    private Long reviewnum;

    private Long mno;

    private Long mid;

    private String nickname;

    private String email;

    private int grade;

    private String text;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
