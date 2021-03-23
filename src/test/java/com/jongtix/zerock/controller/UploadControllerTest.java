package com.jongtix.zerock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UploadControllerTest {

    @Value("${com.jongtix.zerock.upload.path}")
    private String uploadPath;

    private String sampleImagePath;

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

        sampleImagePath = uploadPath + File.separator + "sample";
    }

    @DisplayName("파일_삭제_테스트")
    @Test
    void removeFile() throws Exception {
        //given
        Path dir = FileSystems.getDefault().getPath(uploadPath);
        Path tempFile = Files.createTempFile(dir, "", "");
        File file = tempFile.toFile();
        String fileName = file.getName();
        File tempThumbnail = new File(file.getParent(), "s_" + fileName);
        tempThumbnail.createNewFile();

        String url = "http://localhost:" + port + "/removeFile";

        //when
        ResultActions resultActions = mvc.perform(
                post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("fileName", fileName)
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("true"));
    }

    @DisplayName("파일_가져오기_테스트")
    @Test
    void getFile() throws Exception {
        //given
        String orgFileName1 = "sample" + File.separator + "사과1.jpg";
        String url = "http://localhost:" + port + "/display";

        //when
        ResultActions resultActions = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("fileName", orgFileName1)
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("파일_가져오기_실패_테스트")
    @Test
    void getFileFailTest() throws Exception {
        //given
        String orgFileName1 = "unexpectedFile.txt";
        String url = "http://localhost:" + port + "/display";

        //when
        ResultActions resultActions = mvc.perform(
                get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .param("fileName", orgFileName1)
        );

        //then
        resultActions.andDo(print())
                .andExpect(status().is5xxServerError());
    }

    //(수정 필요)썸네일 저장 활성화 할 경우 테스트 실패함
    @DisplayName("파일_업로드_기능_테스트")
    @Test
    void uploadFile() throws Exception {
        //given
        String orgFileName1 = "사과1.jpg";
        MockMultipartFile file1 = new MockMultipartFile(
                "uploadFiles",
                orgFileName1,
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File(sampleImagePath + File.separator + orgFileName1))
        );

        String orgFileName2 = "사과2.jpg";
        MockMultipartFile file2 = new MockMultipartFile(
                "uploadFiles",
                orgFileName2,
                MediaType.IMAGE_GIF_VALUE,
                new FileInputStream(new File(sampleImagePath + File.separator + orgFileName2))
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