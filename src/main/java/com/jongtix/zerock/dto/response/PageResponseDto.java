package com.jongtix.zerock.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDto<DTO, EN> { //다양한 곳에서 사용할 수 있도록 제네릭 타입을 이용

    //DTO 리스트
    private List<DTO> dtoList;

    //총 페이지 번호
    private int totalPage;

    //현재 페이지 번호
    private int page;

    //목록 사이즈
    private int size;

    //시작 페이지 번호
    private int start;

    //끝 페이지 번호
    private int end;

    //이전
    private boolean prev;

    //다음
    private boolean next;

    //페이지 번호 목록
    private List<Integer> pageList;

    public PageResponseDto(Page<EN> result, Function<EN, DTO> fn) { //Function<EN, DTO>: 엔티티 객체들을 DTO로 변환해 주는 기능을 하는 function
                                                                    //나중에 어떤 종류의 Page<E> 타입이 생성되더라도 PageResultDTO를 이용해서 처리할 수 있다는 장점이 있음

        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();

        makePageList(result.getPageable());

    }

    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1;   //0부터 시작하므로 + 1
        this.size = pageable.getPageSize();

        //temp end page
        int tempEnd = ((int) Math.ceil(page / 10.0)) * 10;

        start = tempEnd - 9;

        prev = start > 1;

        end = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end)
                .boxed()    //primitive type을 boxing(wrapper class로 변경)
                .collect(Collectors.toList());
    }

}
