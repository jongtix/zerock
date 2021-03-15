package com.jongtix.zerock.domain.board;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ReplyRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @AfterEach
    void tearDown() {
        replyRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Transactional
    @DisplayName("댓글_저장_테스트_단일_게시판_단일_댓글")
    @Test
    void insert_reply() {
        //given
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);
        String email = "email";
        String password = "password";
        String name = "name";
        Member member = memberRepository.save(
                Member.builder()
                        .email(email)
                        .password(password)
                        .name(name)
                        .build()
        );

        String title = "title";
        String content = "content";
        Board board = boardRepository.save(
                Board.builder()
                        .title(title)
                        .content(content)
                        .writer(member)
                        .build()
        );

        String text = "text";
        String replyer = "replyer";

        //when
        Long rno = replyRepository.save(
                Reply.builder()
                        .text(text)
                        .replyer(replyer)
                        .board(board)
                        .build()
        ).getRno();

        //then
        Reply reply = replyRepository.findById(rno).orElseThrow(() -> new IllegalArgumentException("해당하는 댓글이 없습니다. rno: " + rno));
        /**
         * Eager Loading으로 사용시
         * ManyToOne 관계에 있는 Entity를 Join하여 조회
         * Hibernate:
         *     select
         *         reply0_.rno as rno1_3_0_,
         *         reply0_.moddate as moddate2_3_0_,
         *         reply0_.regdate as regdate3_3_0_,
         *         reply0_.board_bno as board_bn6_3_0_,
         *         reply0_.replyer as replyer4_3_0_,
         *         reply0_.text as text5_3_0_,
         *         board1_.bno as bno1_0_1_,
         *         board1_.moddate as moddate2_0_1_,
         *         board1_.regdate as regdate3_0_1_,
         *         board1_.content as content4_0_1_,
         *         board1_.title as title5_0_1_,
         *         board1_.writer_email as writer_e6_0_1_,
         *         member2_.email as email1_2_2_,
         *         member2_.moddate as moddate2_2_2_,
         *         member2_.regdate as regdate3_2_2_,
         *         member2_.name as name4_2_2_,
         *         member2_.password as password5_2_2_
         *     from
         *         reply reply0_
         *     left outer join
         *         board board1_
         *             on reply0_.board_bno=board1_.bno
         *     left outer join
         *         member member2_
         *             on board1_.writer_email=member2_.email
         *     where
         *         reply0_.rno=?
         */
        assertThat(reply.getText()).isEqualTo(text);
        assertThat(reply.getReplyer()).isEqualTo(replyer);
        assertThat(reply.getRegDate()).isAfter(now);
        assertThat(reply.getModDate()).isAfter(now);
        assertThat(reply.getBoard().getTitle()).isEqualTo(title);
        assertThat(reply.getBoard().getContent()).isEqualTo(content);
        assertThat(reply.getBoard().getWriter().getEmail()).isEqualTo(email);
        assertThat(reply.getBoard().getWriter().getPassword()).isEqualTo(password);
        assertThat(reply.getBoard().getWriter().getName()).isEqualTo(name);
    }

}