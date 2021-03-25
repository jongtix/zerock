package com.jongtix.zerock.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyRequestDto {

    private Long rno;

    private String text;

    private String replyer;

    private Long bno;   //게시글 번호

}
