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
public class NoteRequestDto {

    private Long num;

    private String title;

    private String content;

    private String writerEmail;

}
