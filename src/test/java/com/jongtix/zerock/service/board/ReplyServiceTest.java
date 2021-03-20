package com.jongtix.zerock.service.board;

import com.jongtix.zerock.domain.board.*;
import com.jongtix.zerock.dto.request.ReplyRequestDto;
import com.jongtix.zerock.dto.response.ReplyResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ReplyServiceTest {

    @Autowired
    private ReplyService replyService;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        replyRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("댓글_등록_테스트")
    @Test
    void register() {
        //given
        String text = "text";
        String replyer = "replyer";
        Board board = boardRepository.save(
                Board.builder()
                        .build()
        );

        //when
        Long rno = replyService.register(
                ReplyRequestDto.builder()
                        .text(text)
                        .replyer(replyer)
                        .bno(board.getBno())
                        .build()
        );

        //then
        Reply reply = replyRepository.findById(rno).orElseThrow(() -> new IllegalArgumentException("해당하는 댓글이 없습니다. rno: " + rno));
        assertThat(reply.getText()).isEqualTo(text);
        assertThat(reply.getReplyer()).isEqualTo(replyer);
        assertThat(reply.getBoard().getBno()).isEqualTo(board.getBno());
    }

    @DisplayName("댓글_목록_조회_테스트")
    @Test
    void getList() {
        //given
        Board board = boardRepository.save(
                Board.builder()
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
        List<ReplyResponseDto> list = replyService.getList(board.getBno());

        //then
        assertThat(list.get(0).getText()).isEqualTo(text1);
        assertThat(list.get(0).getReplyer()).isEqualTo(replyer1);
        assertThat(list.get(1).getText()).isEqualTo(text2);
        assertThat(list.get(1).getReplyer()).isEqualTo(replyer2);
    }

    @DisplayName("댓글_수정_테스트")
    @Test
    void modify() {
        //given
        Board board = boardRepository.save(
                Board.builder()
                        .build()
        );

        String text = "text";
        String replyer = "replyer";
        Long rno = replyRepository.save(
                Reply.builder()
                        .text(text)
                        .replyer(replyer)
                        .board(board)
                        .build()
        ).getRno();

        String expectedText = "expectedText";
        String expectedReplyer = "expectedReplyer";

        //when
        replyService.modify(
                ReplyRequestDto.builder()
                        .rno(rno)
                        .text(expectedText)
                        .replyer(expectedReplyer)
                        .bno(board.getBno())
                        .build()
        );

        //then
        Reply reply = replyRepository.findById(rno).orElseThrow(() -> new IllegalArgumentException("해당하는 댓글이 없습니다. rno: " + rno));
        assertThat(reply.getText()).isEqualTo(expectedText);
        assertThat(reply.getReplyer()).isEqualTo(expectedReplyer);
    }

    @DisplayName("댓글_삭제_테스트")
    @Test
    void remove() {
        //given
        Board board = boardRepository.save(
                Board.builder()
                        .build()
        );

        String text = "text";
        String replyer = "replyer";
        Long rno = replyRepository.save(
                Reply.builder()
                        .text(text)
                        .replyer(replyer)
                        .board(board)
                        .build()
        ).getRno();

        //when
        replyService.remove(rno);

        //then
        assertThrows(NoSuchElementException.class, () -> {
            replyRepository.findById(rno).get();
        });
    }
}