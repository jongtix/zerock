package com.jongtix.zerock.service.board;

import com.jongtix.zerock.domain.board.Board;
import com.jongtix.zerock.domain.board.BoardRepository;
import com.jongtix.zerock.domain.board.Member;
import com.jongtix.zerock.domain.board.MemberRepository;
import com.jongtix.zerock.dto.request.BoardRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BoardServiceImplTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
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

        memberRepository.save(Member.builder()
                .email(email)
                .build());

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

}