package com.jongtix.zerock.service.board;

import com.jongtix.zerock.domain.board.Board;
import com.jongtix.zerock.domain.board.Reply;
import com.jongtix.zerock.domain.board.ReplyRepository;
import com.jongtix.zerock.dto.request.ReplyRequestDto;
import com.jongtix.zerock.dto.response.ReplyResponseDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.internal.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    @Override
    public Long register(ReplyRequestDto requestDto) {

        Reply reply = dtoToEntity(requestDto);

        return replyRepository.save(reply).getRno();
    }

    @Override
    public List<ReplyResponseDto> getList(Long bno) {

        List<Reply> result = replyRepository.getRepliesByBoardOrderByRno(
                Board.builder()
                        .bno(bno)
                        .build()
        );

        return result.stream().map(entity -> entityToDto(entity)).collect(Collectors.toList());
    }

    @Override
    public void modify(ReplyRequestDto requestDto) {

        Reply reply = dtoToEntity(requestDto);

        replyRepository.save(reply);

    }

    @Override
    public void remove(Long rno) {
        replyRepository.deleteById(rno);
    }
}
