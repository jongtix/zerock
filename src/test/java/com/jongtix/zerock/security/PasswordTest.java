package com.jongtix.zerock.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class PasswordTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("BCryptPasswordEncoder_사용가능한_값_확인_테스트")
    @Test
    public void testEncode() {
        String password = "1111";

        String encryptPassword = passwordEncoder.encode(password);

        boolean matchResult = passwordEncoder.matches(password, encryptPassword);

        System.out.println(encryptPassword);
        assertThat(matchResult).isTrue();
    }

}
