package com.jongtix.zerock.domain.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ClubMemberRepositoryTest {

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        clubMemberRepository.deleteAll();
    }

    @DisplayName("ClubMember_저장_테스트")
    @Transactional
    @Test
    void insertMember() {
        //given
        String email = "user@zerock.org";
        String name = "name";
        String password = passwordEncoder.encode("password");
        boolean fromSocial = false;

        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .name(name)
                .password(password)
                .fromSocial(fromSocial)
                .build();

        clubMember.addMemberRole(Role.USER);

        clubMemberRepository.save(clubMember);

        //when
        List<ClubMember> clubMemberList = clubMemberRepository.findAll();

        //then
        assertThat(clubMemberList.size()).isEqualTo(1);
        assertThat(clubMemberList.get(0).getEmail()).isEqualTo(email);
        assertThat(clubMemberList.get(0).getName()).isEqualTo(name);
        assertThat(clubMemberList.get(0).getPassword()).isEqualTo(password);
        assertThat(clubMemberList.get(0).isFromSocial()).isEqualTo(fromSocial);
        assertThat(clubMemberList.get(0).getRoleSet().size()).isEqualTo(1);
    }

}