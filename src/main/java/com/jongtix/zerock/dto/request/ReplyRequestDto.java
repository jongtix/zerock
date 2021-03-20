package com.jongtix.zerock.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyRequestDto {

    private Long rno;

    private String text;

    private String replyer;

    private Long bno;   //게시글 번호

}
