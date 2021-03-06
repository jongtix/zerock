package com.jongtix.zerock.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data   //Getter/Setter + toString() + equals() + hashCode() 자동 생성
@Builder(toBuilder = true)
public class ThymeleafDto {

    private Long sno;

    private String first;

    private String last;

    private LocalDateTime regTime;

}
