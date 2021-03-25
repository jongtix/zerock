package com.jongtix.zerock.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
public class GuestbookDto {

    private Long gno;

    private String title;

    private String content;

    private String writer;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

}
