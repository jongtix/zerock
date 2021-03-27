package com.jongtix.zerock.dto.response;

import com.jongtix.zerock.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieImageResponseDto {

    private String uuid;

    private String imgName;

    private String path;

    public String getImageURL() {
        try {
            return URLEncoder.encode(path + File.separator + uuid + Constants.FILE_NAME_SEPARATOR + imgName, Constants.DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getThumbnailURL() {
        try {
            return URLEncoder.encode(path + File.separator + Constants.THUMBNAIL_PREFIX + uuid + Constants.FILE_NAME_SEPARATOR + imgName, Constants.DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
