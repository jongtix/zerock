package com.jongtix.zerock.service;

import com.jongtix.zerock.domain.guestbook.Guestbook;
import com.jongtix.zerock.dto.GuestbookDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.dto.response.PageResponseDto;

public interface GuestbookService {

    Long register(GuestbookDto guestbookDto);

    PageResponseDto<GuestbookDto, Guestbook> getList(PageRequestDto requestDto);

    default Guestbook dtoToEntity(GuestbookDto guestbookDto) {   //인터페이스에 구현체 사용 예, default 선언 필요
        return Guestbook.builder()
                .gno(guestbookDto.getGno())
                .title(guestbookDto.getTitle())
                .content(guestbookDto.getContent())
                .writer(guestbookDto.getWriter())
                .build();
    };

    default GuestbookDto entityToDto(Guestbook entity) {
        return GuestbookDto.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

}
