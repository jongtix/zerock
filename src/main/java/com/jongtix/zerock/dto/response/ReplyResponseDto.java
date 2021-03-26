package com.jongtix.zerock.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyResponseDto {

    private Long rno;

    private String text;

    private String replyer;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
