package com.jongtix.zerock.service.board;

import com.jongtix.zerock.domain.board.*;
import com.jongtix.zerock.dto.request.BoardRequestDto;
import com.jongtix.zerock.dto.request.PageRequestDto;
import com.jongtix.zerock.dto.response.BoardResponseDto;
import com.jongtix.zerock.dto.response.PageResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @AfterEach
    void tearDown() {
        replyRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("게시글_등록_테스트")
    @Test
    void testRegister() {
        //given
        String title = "title";
        String content = "content";
        String email = "email";

        //object references an unsaved transient instance - save the transient instance before flushing : com.jongtix.zerock.domain.board.Board.writer -> com.jongtix.zerock.domain.board.Member
        //부모 객체에서 자식 객체를 바인딩하여 한번에 저장하려는데 자식 객체가 아직 데이터 베이스에 저장되지 않았기 때문에 발생
        //출처: https://m.blog.naver.com/PostView.nhn?blogId=rorean&logNo=221479709787&proxyReferer=https:%2F%2Fwww.google.com%2F
        //JPA의 persist를 호출할 때, 해당 객체와 연관된 모든 객체도 영속 상태여야 합니다.
        //(object references an unsaved transient instance -> 이 오류메시지가 그 뜻입니다)
        //출처: https://www.inflearn.com/questions/27526
        memberRepository.save(
                Member.builder()
                        .email(email)
                        .build()
        );

        BoardRequestDto requestDto = BoardRequestDto.builder()
                .title(title)
                .content(content)
                .writerEmail(email)
                .build();

        //when
        Long bno = boardService.register(requestDto);

        //then
        Board board = boardRepository.findById(bno).orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 없습니다. bno: " + bno));
        assertThat(board.getTitle()).isEqualTo(title);
        assertThat(board.getContent()).isEqualTo(content);
        assertThat(board.getWriter().getEmail()).isEqualTo(email);
    }

    @DisplayName("게시글_리스트_조회")
    @Test
    void getList() {
        //given
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);

        String title = "title";
        String content = "content";
        String email = "email";

        Member member = memberRepository.save(
                Member.builder()
                        .email(email)
                        .build()
        );

        Board board = boardRepository.save(
                Board.builder()
                        .title(title)
                        .content(content)
                        .writer(member)
                        .build()
        );

        String text1 = "text1";
        String replyer1 = "replyer1";
        replyRepository.save(
                Reply.builder()
                        .text(text1)
                        .replyer(replyer1)
                        .board(board)
                        .build()
        );

        String text2 = "text2";
        String replyer2 = "replyer2";
        replyRepository.save(
                Reply.builder()
                        .text(text2)
                        .replyer(replyer2)
                        .board(board)
                        .build()
        );

        //when
        PageResponseDto<BoardResponseDto, Object[]> responseDto = boardService.getList(new PageRequestDto());

        //then
        assertThat(responseDto.getDtoList().get(0).getTitle()).isEqualTo(title);
        assertThat(responseDto.getDtoList().get(0).getContent()).isEqualTo(content);
        assertThat(responseDto.getDtoList().get(0).getWriterEmail()).isEqualTo(email);
        assertThat(responseDto.getDtoList().get(0).getRegDate()).isAfter(now);
        assertThat(responseDto.getDtoList().get(0).getModDate()).isAfter(now);
        assertThat(responseDto.getDtoList().get(0).getReplyCount()).isEqualTo(2);
    }

    @DisplayName("게시글_상세조회")
    @Test
    void getBoard() {
        //given
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);

        String title = "title";
        String content = "content";
        String email = "email";

        Member member = memberRepository.save(
                Member.builder()
                        .email(email)
                        .build()
        );

        Board board = boardRepository.save(
                Board.builder()
                        .title(title)
                        .content(content)
                        .writer(member)
                        .build()
        );

        String text1 = "text1";
        String replyer1 = "replyer1";
        replyRepository.save(
                Reply.builder()
                        .text(text1)
                        .replyer(replyer1)
                        .board(board)
                        .build()
        );

        String text2 = "text2";
        String replyer2 = "replyer2";
        replyRepository.save(
                Reply.builder()
                        .text(text2)
                        .replyer(replyer2)
                        .board(board)
                        .build()
        );

        //when
        BoardResponseDto responseDto = boardService.getBoard(board.getBno());

        //then
        assertThat(responseDto.getTitle()).isEqualTo(title);
        assertThat(responseDto.getContent()).isEqualTo(content);
        assertThat(responseDto.getWriterEmail()).isEqualTo(email);
        assertThat(responseDto.getRegDate()).isAfter(now);
        assertThat(responseDto.getModDate()).isAfter(now);
        assertThat(responseDto.getReplyCount()).isEqualTo(2);
    }

    @DisplayName("게시글_삭제")
    @Test
    void removeWithReplies() {
        //given
        String title = "title";
        String content = "content";
        String email = "email";

        Member member = memberRepository.save(
                Member.builder()
                        .email(email)
                        .build()
        );

        Board board = boardRepository.save(
                Board.builder()
                        .title(title)
                        .content(content)
                        .writer(member)
                        .build()
        );

        String text1 = "text1";
        String replyer1 = "replyer1";
        Long rno1 = replyRepository.save(
                Reply.builder()
                        .text(text1)
                        .replyer(replyer1)
                        .board(board)
                        .build()
        ).getRno();

        String text2 = "text2";
        String replyer2 = "replyer2";
        Long rno2 = replyRepository.save(
                Reply.builder()
                        .text(text2)
                        .replyer(replyer2)
                        .board(board)
                        .build()
        ).getRno();

        Long bno = board.getBno();

        //when
        boardService.removeWithReplies(bno);

        //then
        assertThrows(NoSuchElementException.class, () -> {
            boardRepository.findById(bno).get();
        });
        assertThrows(NoSuchElementException.class, () -> {
            replyRepository.findById(rno1).get();
        });
        assertThrows(NoSuchElementException.class, () -> {
            replyRepository.findById(rno2).get();
        });
    }

    @DisplayName("게시글_수정")
    @Test
    void modify() {
        //given
        String title = "title";
        String content = "content";
        String email = "email";

        Member member = memberRepository.save(
                Member.builder()
                        .email(email)
                        .build()
        );

        Board board = boardRepository.save(
                Board.builder()
                        .title(title)
                        .content(content)
                        .writer(member)
                        .build()
        );

        Long bno = board.getBno();
        String expectedTitle = "expectedTitle";
        String expectedContent = "expectedContent";

        BoardRequestDto requestDto = BoardRequestDto.builder()
                .bno(bno)
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        //when
        boardService.modify(requestDto);

        //then
        Board resultBoard = boardRepository.findById(bno).orElseThrow(() -> new IllegalArgumentException("해당하는 게시글이 없습니다. bno: " + bno));
        assertThat(resultBoard.getTitle()).isEqualTo(expectedTitle);
        assertThat(resultBoard.getContent()).isEqualTo(expectedContent);
        assertThat(resultBoard.getRegDate()).isBefore(resultBoard.getModDate());
    }

}