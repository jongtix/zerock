package com.jongtix.zerock.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDto {

    private int page;

    private int size;

    private String type;    //검색 타입 추가

    private String keyword; //검색 키워드 추가

    public PageRequestDto() {
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(this.page - 1, this.size, sort);
        /**
         * Spring Data JPA 이용 시 @PageableDefault라는 어노테이션으로 Pageable 타입을 이용하거나,
         * application.yml(properties)에 0이 아닌 1부터 페이지 번호를 받을 수 있게 처리할 수 있음
         */
    }

}
