package com.jongtix.zerock.service.board;

import com.jongtix.zerock.domain.board.Board;
import com.jongtix.zerock.domain.board.BoardRepository;
import com.jongtix.zerock.dto.request.BoardRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository;

    @Override
    public Long register(BoardRequestDto requestDto) {

        log.info(requestDto);

        Board board = dtoToEntity(requestDto);

        repository.save(board);

        return board.getBno();
    }

}
