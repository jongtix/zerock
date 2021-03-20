package com.jongtix.zerock.service.board;

import com.jongtix.zerock.domain.board.Board;
import com.jongtix.zerock.domain.board.BoardRepository;
import com.jongtix.zerock.domain.board.Member;
import com.jongtix.zerock.domain.board.ReplyRepository;
import com.jongtix.zerock.dto.request.BoardRequestDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.dto.response.BoardResponseDto;
import com.jongtix.zerock.dto.response.PageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardRequestDto requestDto) {

        log.info(requestDto);

        Board board = dtoToEntity(requestDto);

        boardRepository.save(board);

        return board.getBno();
    }

    /**
     * PageRequestDto를 받아 entityToDto 메서드를 활용해 Object[]를 PageResponseDto로 변환하는 것이 핵심 내용
     * @param requestDto
     * @return
     */
    @Override
    public PageResponseDto<BoardResponseDto, Object[]> getList(PageRequestDto requestDto) {

        log.info(requestDto);

        Pageable pageable = requestDto.getPageable(Sort.by("bno"));

        //AS-IS
        //QuerydslRepositorySupport 적용 전
//        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        //TO-BE
        //QuerydslRepositorySupport 적용 후
        Page<Object[]> result = boardRepository.searchPage(
                requestDto.getType(),
                requestDto.getKeyword(),
                requestDto.getPageable(Sort.by("bno").descending())
        );

        Function<Object[], BoardResponseDto> function = (entity -> entityToDto((Board) entity[0], (Member) entity[1], (Long) entity[2]));

        return new PageResponseDto<>(result, function);
    }

    /**
     * bno를 받아 entityToDto 메서드를 활용해 Object를 PageResponseDto로 변환
     * @param bno
     * @return
     */
    @Override
    public BoardResponseDto getBoard(Long bno) {

        Object[] result = (Object[]) boardRepository.getBoardByBno(bno);

        return entityToDto((Board) result[0], (Member) result[1], (Long) result[2]);
    }

    @Transactional  //게시물을 삭제하려면 FK로 게시물을 참조하고 있는 reply 테이블 역시 삭제해야만 하므로 꼭 두 과정이 하나의 트랜잭션으로 처리되어야 함
    @Override
    public void removeWithReplies(Long bno) {   //삭제 기능 구현, 트랜잭션 추가

        //1. 댓글 삭제
        replyRepository.deleteByBno(bno);

        //2. 게시글 삭제
        boardRepository.deleteById(bno);

    }

//    JPA의 영속성 컨텍스트를 이용하여 수정
//    AS-IS
//    @Override
//    public void modify(BoardRequestDto requestDto) {
//
//        Long bno = requestDto.getBno();
//
////        Board board = boardRepository.findById(bno).orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 없습니다. bno: " + bno));
//        Board board = boardRepository.getOne(bno);  //findById 메서드 대신 필요한 순간까지 로딩을 지연하는 방식인 getOne 메서드 사용
//
//        board.changeTitle(requestDto.getTitle());
//        board.changeContent(requestDto.getContent());
//
//        boardRepository.save(board);
//    }

//    TO-BE
    @Transactional
    @Override
    public Long modify(BoardRequestDto requestDto) {

        Long bno = requestDto.getBno();

//        Board board = boardRepository.findById(bno).orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 없습니다. bno: " + bno));
        Board board = boardRepository.getOne(bno);  //findById 메서드 대신 필요한 순간까지 로딩을 지연하는 방식인 getOne 메서드 사용

        board.changeTitle(requestDto.getTitle());
        board.changeContent(requestDto.getContent());

        return board.getBno();
    }

}
