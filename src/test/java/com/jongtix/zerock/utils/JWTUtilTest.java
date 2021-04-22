package com.jongtix.zerock.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

class JWTUtilTest {

    private JWTUtil jwtUtil;

    @BeforeEach
    void setup() {
        jwtUtil = new JWTUtil();
    }

    @Test
    void generateToken() throws Exception {
        //given
        String email = "user@zerock.org";

        System.out.println(new Date().getTime());
        System.out.println(Date.from(ZonedDateTime.now().plusMinutes(60 * 24 * 30).toInstant()).getTime());

        String result = jwtUtil.generateToken(email);

        System.out.println(new Date().getTime());
        System.out.println(Date.from(ZonedDateTime.now().plusMinutes(60 * 24 * 30).toInstant()).getTime());

        System.out.println(result);
    }

    @Test
    void validateAndExtract() throws Exception {
        //given
        String email = "user@zerock.org";

        String result = jwtUtil.generateToken(email);

        Thread.sleep(5000); //만료 시간 테스트

        String resultEmail = jwtUtil.validateAndExtract(result);

        System.out.println(resultEmail);

        assertThat(resultEmail).isEqualTo(email);
    }
}