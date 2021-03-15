package com.jongtix.zerock.domain.board;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @DisplayName("멤버_저장_테스트")
    @Test
    void insert_members() {
        //given
        String email = "email";
        String password = "password";
        String name = "name";
        LocalDateTime now = LocalDateTime.now().minusSeconds(1);

        //when
        memberRepository.save(
                Member.builder()
                        .email(email)
                        .password(password)
                        .name(name)
                        .build()
        );

        //then
        Member member = memberRepository.findById(email).orElseThrow(() -> new IllegalArgumentException("해당하는 사용자가 없습니다. email: " + email));
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getPassword()).isEqualTo(password);
        assertThat(member.getName()).isEqualTo(name);
        assertThat(member.getRegDate()).isAfter(now);
        assertThat(member.getModDate()).isAfter(now);
    }

}