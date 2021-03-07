package com.jongtix.zerock.domain.guestbook;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

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
        LocalDateTime now = LocalDateTime.now();
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

        LocalDateTime beforeUpdate = LocalDateTime.now();
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

}