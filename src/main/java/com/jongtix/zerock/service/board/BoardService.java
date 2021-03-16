package com.jongtix.zerock.service.board;

import com.jongtix.zerock.domain.board.Board;
import com.jongtix.zerock.domain.board.Member;
import com.jongtix.zerock.dto.request.BoardRequestDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.dto.response.BoardResponseDto;
import com.jongtix.zerock.dto.response.PageResponseDto;

public interface BoardService {

    Long register(BoardRequestDto requestDto);

    PageResponseDto<BoardResponseDto, Object[]> getList(PageRequestDto requestDto);

    BoardResponseDto getBoard(Long bno);

    void removeWithReplies(Long bno);   //삭제 기능

    void modify(BoardRequestDto requestDto);

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

    default BoardResponseDto entityToDto(Board board, Member member, Long replyCount) {
        return BoardResponseDto.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue())  //Long -> int
                .build();
    }

}
