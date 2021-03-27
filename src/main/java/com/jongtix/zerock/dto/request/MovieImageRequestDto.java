package com.jongtix.zerock.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieImageRequestDto {

    private String uuid;

    private String imgName;

    private String path;

}
