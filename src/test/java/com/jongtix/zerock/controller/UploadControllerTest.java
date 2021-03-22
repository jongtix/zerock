package com.jongtix.zerock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UploadControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

//    @DisplayName("파일_가져오기_테스트")
//    @Test
//    void getFile() throws Exception {
//        //given
//        String url = "http://localhost:" + port + "/display";
//
//        //when
//        ResultActions resultActions = mvc.perform(
//                get(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .characterEncoding("UTF-8")
////                .param("fileName", )
//        );
//
//        //then
//        resultActions.andDo(print())
//                .andExpect(status().is5xxServerError());
//    }
//
//    @DisplayName("파일_가져오기_실패_테스트")
//    @Test
//    void getFileFailTest() throws Exception {
//        //given
//        String url = "http://localhost:" + port + "/display";
//
//        //when
//        ResultActions resultActions = mvc.perform(
//                get(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .characterEncoding("UTF-8")
//        );
//
//        //then
//        resultActions.andDo(print())
//                .andExpect(status().is5xxServerError());
//    }

    //(수정 필요)썸네일 저장 활성화 할 경우 테스트 실패함
    @DisplayName("파일_업로드_기능_테스트")
    @Test
    void uploadFile() throws Exception {
        //given
        String orgFileName1 = "hello.jpg";
        MockMultipartFile file1 = new MockMultipartFile(
                "uploadFiles",
                orgFileName1,
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );

        String orgFileName2 = "hello2.gif";
        MockMultipartFile file2 = new MockMultipartFile(
                "uploadFiles",
                orgFileName2,
                MediaType.IMAGE_GIF_VALUE,
                "Hello, World!".getBytes()
        );

        String url = "http://localhost:" + port + "/uploadAjax";

        //when
        ResultActions resultActions = mvc.perform(
                multipart(url)
                        .file(file1)
                        .file(file2)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].fileName").value(orgFileName1))
                .andExpect(jsonPath("$.[0].folderPath").value(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))))
                .andExpect(jsonPath("$.[1].fileName").value(orgFileName2))
                .andExpect(jsonPath("$.[1].folderPath").value(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))));
        System.out.println(jsonPath("$.[0].fileName").toString());
    }

    @DisplayName("파일_업로드_기능_실패_테스트")
    @Test
    void uploadFileFailTest() throws Exception {
        //given
        MockMultipartFile file1 = new MockMultipartFile(
                "uploadFiles",
                "hello1.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes(StandardCharsets.UTF_8)
        );

        MockMultipartFile file2 = new MockMultipartFile(
                "uploadFiles",
                "hello2.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes(StandardCharsets.UTF_8)
        );

        String url = "http://localhost:" + port + "/uploadAjax";

        //when
        ResultActions resultActions = mvc.perform(
                multipart(url)
                        .file(file1)
                        .file(file2)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isForbidden());
    }
}