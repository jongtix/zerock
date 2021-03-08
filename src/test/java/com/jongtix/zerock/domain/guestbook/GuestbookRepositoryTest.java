package com.jongtix.zerock.domain.guestbook;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
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

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GuestbookRepositoryTest {

    @Autowired
    private GuestbookRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @DisplayName("기본_insert_테스트")
    @Test
    void insert_dummies_with_querydsl() {
        //given
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);
        String title = "title";
        String content = "content";
        String writer = "writer";

        //when
        repository.save(
                Guestbook.builder()
                        .title(title)
                        .content(content)
                        .writer(writer)
                        .build()
        );

        //then
        List<Guestbook> guestbooks = repository.findAll();
        assertThat(guestbooks.get(0).getTitle()).isEqualTo(title);
        assertThat(guestbooks.get(0).getContent()).isEqualTo(content);
        assertThat(guestbooks.get(0).getWriter()).isEqualTo(writer);
        assertThat(guestbooks.get(0).getRegDate()).isAfter(now);
        assertThat(guestbooks.get(0).getModDate()).isAfter(now);
    }

    @DisplayName("수정_시간_테스트")
    @Test
    void update_time_test() {
        //given
        String writer = "writer";

        Guestbook guestbook = repository.save(
                Guestbook.builder()
                        .title("title")
                        .content("content")
                        .writer(writer)
                        .build()
        );

        LocalDateTime beforeUpdate = LocalDateTime.now().minusSeconds(1);
        String expectedTitle = "expectedTitle";
        String expectedContent = "expectedContent";

        //when
        guestbook.changeTitle(expectedTitle);
        guestbook.changeContent(expectedContent);
        repository.save(guestbook);

        //then
        List<Guestbook> guestbooks = repository.findAll();
        assertThat(guestbooks.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(guestbooks.get(0).getContent()).isEqualTo(expectedContent);
        assertThat(guestbooks.get(0).getWriter()).isEqualTo(writer);
        assertThat(guestbooks.get(0).getRegDate()).isBefore(beforeUpdate);
        assertThat(guestbooks.get(0).getModDate()).isAfter(beforeUpdate);
    }

    @DisplayName("querydsl_단일_항목_검색_테스트")
    @Test
    void select_one_condition_with_querydsl() {
        /**
         * Querydsl을 사용할 때 JPAQueryFactory, BooleanBuilder 사용 가능
         */
        //given
        IntStream.rangeClosed(1, 15).forEach(
                i -> repository.save(
                        Guestbook.builder()
                                .title("제목" + i)
                                .content("내용" + i)
                                .writer("작성자" + i)
                                .build()
                )
        );

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = "1";

        //when
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression expression = qGuestbook.title.contains(keyword);  //원하는 조건은 필드 값과 같이 결합해서 생성
                                                                            //BooleanBuilder 안에 들어가는 값은 Querydsl의 Predicate 타입이어야 함
        builder.and(expression);
        Page<Guestbook> guestbooks = repository.findAll(builder, pageable); //QuerydslPredicateExcutor 인터페이스의 findALl()

        //then
        assertThat(guestbooks.getContent().get(0).getTitle()).contains(keyword);
        assertThat(guestbooks.getContent().get(1).getTitle()).contains(keyword);
        assertThat(guestbooks.getContent().get(2).getTitle()).contains(keyword);
        assertThat(guestbooks.getContent().get(3).getTitle()).contains(keyword);
        assertThat(guestbooks.getContent().get(4).getTitle()).contains(keyword);
        assertThat(guestbooks.getContent().get(5).getTitle()).contains(keyword);
        assertThat(guestbooks.getContent().get(6).getTitle()).contains(keyword);
    }

    @DisplayName("querydsl_다중_항목_검색_테스트")
    @Test
    void select_multi_condition_with_querydsl() {
        //given
        IntStream.rangeClosed(1, 15)
                .forEach(
                        i -> repository.save(
                                Guestbook.builder()
                                        .title("제목" + i)
                                        .content("내용" + i)
                                        .writer("작성자" + i)
                                        .build()
                        )
                );

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        //when
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression expressionTitle = qGuestbook.title.contains(keyword);
        BooleanExpression expressionContent = qGuestbook.content.contains(keyword);
        BooleanExpression expressionAll = expressionTitle.or(expressionContent);
        builder.and(expressionAll);
        builder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> guestbooks = repository.findAll(builder, pageable);

        //then
        assertThat(guestbooks.getTotalElements()).isEqualTo(7L);
        assertThat(guestbooks.getContent().get(0).getTitle()).contains(keyword);
        assertThat(guestbooks.getContent().get(1).getTitle()).contains(keyword);
        assertThat(guestbooks.getContent().get(2).getTitle()).contains(keyword);
    }

}