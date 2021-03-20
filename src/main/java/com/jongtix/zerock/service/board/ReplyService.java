package com.jongtix.zerock.service.board;

import com.jongtix.zerock.domain.board.Board;
import com.jongtix.zerock.domain.board.Reply;
import com.jongtix.zerock.dto.request.ReplyRequestDto;
import com.jongtix.zerock.dto.response.ReplyResponseDto;

import java.util.List;

public interface ReplyService {

    //댓글 등록
    Long register(ReplyRequestDto requestDto);

    //특정 게시물의 댓글 목록
    List<ReplyResponseDto> getList(Long bno);

    //댓글 수정
    void modify(ReplyRequestDto requestDto);

    //댓글 삭제
    void remove(Long rno);

    //ReplyRequestDto를 Reply Entity 객체로 변환(Board 객체의 처리가 수반됨)
    default Reply dtoToEntity(ReplyRequestDto replyRequestDto) {
        return Reply.builder()
                .rno(replyRequestDto.getRno())
                .text(replyRequestDto.getText())
                .replyer(replyRequestDto.getReplyer())
                .board(
                        Board.builder()
                                .bno(replyRequestDto.getBno())
                                .build()
                )
                .build();
    }

    default ReplyResponseDto entityToDto(Reply reply) {
        return ReplyResponseDto.builder()
                .rno(reply.getRno())
                .text(reply.getText())
                .replyer(reply.getReplyer())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();
    }

}
