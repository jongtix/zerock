package com.jongtix.zerock.controller;

import com.jongtix.zerock.dto.response.UploadResponseDto;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    @Value("${com.jongtix.zerock.upload.path}") //application.yml의 변수
    private String uploadPath;

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName) {

        String srcFileName = null;

        try {
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();
            if (!result) {
                return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
            }

//            File thumbnail = new File(uploadPath + File.separator + "s_" + srcFileName);
            File thumbnail = new File(file.getParent(), "s_" + file.getName());
            result = thumbnail.delete();
            if (!result) {
                return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName) {

        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");

            log.info("fileName: " + fileName);

            File file = new File(uploadPath + File.separator + srcFileName);

            log.info("file: " + file);

            HttpHeaders header = new HttpHeaders();

            //MIME타입 처리
            //파일의 확장자에 따라서 브라우저에 전송하는 MIME 타입이 달라져야 하는 문제 해결
            header.add("Content-type", Files.probeContentType(file.toPath()));

            //파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header , HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResponseDto>> uploadFile(MultipartFile[] uploadFiles) {

        log.info("uploadFile................");

        List<UploadResponseDto> resultDtoList = new ArrayList<>();

        for (MultipartFile uploadFile : uploadFiles) {

            if (!uploadFile.getContentType().startsWith("image")) {
                log.warn("this file is not image type");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);  //이미지 파일이 아닌 경우 403
            }

            //실제 파일 이름 IE나 Edge는 전체 경로가 들어오므로
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.indexOf("\\") + 1);

            log.info("fileName: " + fileName);

            /**
             * 파일 저장시 고려 사항
             * 1. 업로드된 확장자가 이미지만 가능하도록 검사(첨부파일을 이용한 원격 셀)
             *      -> 쉘 스크립트 등을 이용한 공격에 대비
             * 2. 동일한 이름의 파일이 업로드 된다면 기존 파일을 덮어쓰는 문제
             *      -> 고유한 이름을 생성해서 파일 이름으로 사용(시간 값을 파일 이름에 추가 / UUID를 이용해 고유한 값을 만들어 사용)
             * 3. 업로드된 파일을 저장하는 폴더의 용량
             *      -> 파일이 저장되는 시점의 '년/월/일' 폴더를 따로 생성하여 한 폴더에 너무 많은 파일이 쌓이지 않도록 함
             */
            //날짜 폴더 생성
            String folderPath = makeFolder();

            //파일 구분을 위한 uuid 생성
            String uuid = UUID.randomUUID().toString();

            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            Path savePath = Paths.get(saveName);

            try {
                log.info(saveName);
                //파일 원본 저장
                uploadFile.transferTo(savePath);

                //썸네일 저장
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName;
                log.info(thumbnailSaveName);

                File thumbnailFile = new File(thumbnailSaveName);

                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

                resultDtoList.add(new UploadResponseDto(fileName, uuid, folderPath));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return new ResponseEntity<>(resultDtoList, HttpStatus.OK);
    }

    private String makeFolder() {
        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("//", File.separator);

        File uploadPathFolder = new File(uploadPath, folderPath);

        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }

}
