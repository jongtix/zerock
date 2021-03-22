package com.jongtix.zerock.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
//@AllArgsConstructor
@RequiredArgsConstructor
public class UploadResponseDto implements Serializable {

    private final String fileName;

    private final String uuid;

    private final String folderPath;

    //전체 경로가 필요한 경우
    public String getImageURL() {
        try {
            return URLEncoder.encode(folderPath + File.separator + uuid + "_" + fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    //썸네일 URL
    public String getThumbnailURL() {
        try {
            return URLEncoder.encode(folderPath + File.separator + "s_" + uuid + "_" + fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
