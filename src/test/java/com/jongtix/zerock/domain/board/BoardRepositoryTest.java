package com.jongtix.zerock.domain.board;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @AfterEach
    void tearDown() {
        replyRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Transactional  //no Session error 해결
                    //@Transactional: 해당 메서드를 하나의 '트랜잭션'으로 처리하라는 의미
    @DisplayName("게시판_저장_테스트_단일_멤버_단일_게시판")
    @Test
    void insert_board() {
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

        //when
        Long bno = boardRepository.save(
                Board.builder()
                        .title(title)
                        .content(content)
                        .writer(member)
                        .build()
        ).getBno();

        //then
        Board board = boardRepository.findById(bno).orElseThrow(() -> new IllegalArgumentException("해당하는 게시판이 없습니다. bno: " + bno));
        /**
         * Eager Loading으로 사용시
         * @ManyToOne 관계에 있는 Entity를 Join하여 조회
         * Hibernate:
         *     select
         *         board0_.bno as bno1_0_0_,
         *         board0_.moddate as moddate2_0_0_,
         *         board0_.regdate as regdate3_0_0_,
         *         board0_.content as content4_0_0_,
         *         board0_.title as title5_0_0_,
         *         board0_.writer_email as writer_e6_0_0_,
         *         member1_.email as email1_2_1_,
         *         member1_.moddate as moddate2_2_1_,
         *         member1_.regdate as regdate3_2_1_,
         *         member1_.name as name4_2_1_,
         *         member1_.password as password5_2_1_
         *     from
         *         board board0_
         *     left outer join
         *         member member1_
         *             on board0_.writer_email=member1_.email
         *     where
         *         board0_.bno=?
         *
         * Lazy Loading으로 사용시(with @Transactional)
         * Hibernate:
         *     select
         *         board0_.bno as bno1_0_,
         *         board0_.moddate as moddate2_0_,
         *         board0_.regdate as regdate3_0_,
         *         board0_.content as content4_0_,
         *         board0_.title as title5_0_,
         *         board0_.writer_email as writer_e6_0_
         *     from
         *         board board0_
         * Hibernate:
         *     select
         *         member0_.email as email1_2_,
         *         member0_.moddate as moddate2_2_,
         *         member0_.regdate as regdate3_2_,
         *         member0_.name as name4_2_,
         *         member0_.password as password5_2_
         *     from
         *         member member0_
         */
        assertThat(board.getTitle()).isEqualTo(title);
        assertThat(board.getContent()).isEqualTo(content);
        assertThat(board.getRegDate()).isAfter(now);
        assertThat(board.getModDate()).isAfter(now);
        assertThat(board.getWriter().getEmail()).isEqualTo(email);
        assertThat(board.getWriter().getPassword()).isEqualTo(password);
        assertThat(board.getWriter().getName()).isEqualTo(name);
    }

    @DisplayName("JPQL을_활용하여_게시판_조회")
    @Test
    void select_board_with_jpql() {
        //given
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

        Long bno = boardRepository.save(
                Board.builder()
                        .title(title)
                        .content(content)
                        .writer(member)
                        .build()
        ).getBno();

        //when
//        Board board = (Board) boardRepository.getBoardWithWriter(bno);
        Object[] result = (Object[]) boardRepository.getBoardWithWriter(bno);
        Board boardResult = (Board) result[0];
        Member memberResult = (Member) result[1];

        //then
        assertThat(boardResult.getTitle()).isEqualTo(title);
        assertThat(boardResult.getContent()).isEqualTo(content);
        assertThat(memberResult.getEmail()).isEqualTo(email);
        assertThat(memberResult.getPassword()).isEqualTo(password);
        assertThat(memberResult.getName()).isEqualTo(name);
    }

    @DisplayName("JPQL을_활용하여_게시판_조회2")
    @Test
    void select_board_with_jpql2() {
        //given
        String title = "title";
        String content = "content";

        Board board = boardRepository.save(
                Board.builder()
                        .title(title)
                        .content(content)
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
        List<Object[]> result = boardRepository.getBoardWIthReply(board.getBno());

        //then
        assertThat(((Board) result.get(0)[0]).getTitle()).isEqualTo(title);
        assertThat(((Board) result.get(0)[0]).getContent()).isEqualTo(content);
        assertThat(((Reply) result.get(0)[1]).getText()).isEqualTo(text1);
        assertThat(((Reply) result.get(0)[1]).getReplyer()).isEqualTo(replyer1);
        assertThat(((Board) result.get(1)[0]).getTitle()).isEqualTo(title);
        assertThat(((Board) result.get(1)[0]).getContent()).isEqualTo(content);
        assertThat(((Reply) result.get(1)[1]).getText()).isEqualTo(text2);
        assertThat(((Reply) result.get(1)[1]).getReplyer()).isEqualTo(replyer2);
    }

    @DisplayName("JPQL을_활용하여_게시판_조회3")
    @Test
    void select_board_with_jpql3() {
        //given
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

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        //when
        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        //then
        result.get().forEach(
                row -> {
                    assertThat(((Board) row[0]).getTitle()).isEqualTo(title);
                    assertThat(((Board) row[0]).getContent()).isEqualTo(content);
                    assertThat(((Member) row[1]).getEmail()).isEqualTo(email);
                    assertThat(((Member) row[1]).getPassword()).isEqualTo(password);
                    assertThat(((Member) row[1]).getName()).isEqualTo(name);
                    assertThat(row[2]).isEqualTo(2L);
                }
        );
    }

    @DisplayName("JPQL을_활용하여_게시판_조회4")
    @Test
    void select_board_with_jpql4() {
        //given
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

        Long bno = board.getBno();

        //when
        Object[] result = (Object[]) boardRepository.getBoardByBno(bno);

        //then
        assertThat(((Board) result[0]).getTitle()).isEqualTo(title);
        assertThat(((Board) result[0]).getContent()).isEqualTo(content);
        assertThat(((Member) result[1]).getEmail()).isEqualTo(email);
        assertThat(((Member) result[1]).getPassword()).isEqualTo(password);
        assertThat(((Member) result[1]).getName()).isEqualTo(name);
        assertThat(result[2]).isEqualTo(2L);
    }

    @DisplayName("search1_테스트")
    @Test
    void search1() {
        //given
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
        boardRepository.search1();
    }

    @DisplayName("searchPage_테스트")
    @Test
    void searchPage() {
        //given
        IntStream.rangeClosed(1, 20).forEach(
                i -> memberRepository.save(
                        Member.builder()
                                .email("email" + i)
                                .password("password" + i)
                                .name("name" + i)
                                .build()
                )
        );
        IntStream.rangeClosed(1, 20).forEach(
                i -> replyRepository.save(
                        Reply.builder()
                                .text("text" + i)
                                .replyer("replyer" + i)
                                .board(boardRepository.findById(
                                        boardRepository.save(
                                                Board.builder()
                                                        .title("title" + i)
                                                        .content("content" + i)
                                                        .writer(Member.builder()
                                                                .email("email" + i)
                                                                .build()
                                                        )
                                                        .build()
                                        ).getBno()
                                ).orElseGet(Board::new))
                                .build()
                )
        );

        String type = "t";
        String keyword = "1";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending().and(Sort.by("title").ascending()));

        //when
        Page<Object[]> result = boardRepository.searchPage(type, keyword, pageable);

        //then
        List<Object[]> list = result.getContent();
        assertThat(list.get(0)[2]).isEqualTo(1L);
        assertThat(((Board) (list.get(0)[0])).getTitle()).isEqualTo("title" + 19);
        assertThat(((Board) (list.get(0)[0])).getContent()).isEqualTo("content" + 19);
        assertThat(((Member) (list.get(0)[1])).getEmail()).isEqualTo("email" + 19);
        assertThat(((Member) (list.get(0)[1])).getPassword()).isEqualTo("password" + 19);
        assertThat(((Member) (list.get(0)[1])).getName()).isEqualTo("name" + 19);

        assertThat(((Board) (list.get(9)[0])).getTitle()).isEqualTo("title" + 10);
        assertThat(((Board) (list.get(9)[0])).getContent()).isEqualTo("content" + 10);
        assertThat(((Member) (list.get(9)[1])).getEmail()).isEqualTo("email" + 10);
        assertThat(((Member) (list.get(9)[1])).getPassword()).isEqualTo("password" + 10);
        assertThat(((Member) (list.get(9)[1])).getName()).isEqualTo("name" + 10);

        assertThat(result.getTotalElements()).isEqualTo(11L);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getNumberOfElements()).isEqualTo(10);
    }

}
