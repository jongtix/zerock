package com.jongtix.zerock.domain.moviereview;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MovieMemberRepositoryTest {

    @Autowired
    private MovieMemberRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @DisplayName("영화_멤버_저장_테스트")
    @Test
    void insert_member() {
        //given
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);
        String email = "email";
        String password = "password";
        String nickname = "nickname";

        //when
        MovieMember movieMember = repository.save(
                MovieMember.builder()
                        .email(email)
                        .password(password)
                        .nickname(nickname)
                        .build()
        );

        //then
        assertThat(movieMember.getEmail()).isEqualTo(email);
        assertThat(movieMember.getPassword()).isEqualTo(password);
        assertThat(movieMember.getNickname()).isEqualTo(nickname);
        assertThat(movieMember.getRegDate()).isAfter(now);
        assertThat(movieMember.getModDate()).isAfter(now);
    }
}