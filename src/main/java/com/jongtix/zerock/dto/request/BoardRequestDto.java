package com.jongtix.zerock.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequestDto {

    private Long bno;

    private String title;

    private String content;

    private String writerEmail; //작성자 이메일

    private String writerName;  //작성자 이름

}
