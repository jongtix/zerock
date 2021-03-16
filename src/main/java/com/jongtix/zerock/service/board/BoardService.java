package com.jongtix.zerock.service.board;

import com.jongtix.zerock.domain.board.Board;
import com.jongtix.zerock.domain.board.Member;
import com.jongtix.zerock.dto.request.BoardRequestDto;

public interface BoardService {

    Long register(BoardRequestDto requestDto);

    default Board dtoToEntity(BoardRequestDto requestDto) {
        Member member = Member.builder()
                .email(requestDto.getWriterEmail())
                .name(requestDto.getWriterName())
                .build();

        return Board.builder()
                .bno(requestDto.getBno())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .writer(member)
                .build();
    };

}
