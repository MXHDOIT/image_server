package com.xpu.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Image {
    private Integer imageId;
    private String imageName;
    private Integer size;
    private String uploadTime;
    private String contentType;
    private String path;
    private String md5;

    public Image() {
    }

    public Image(Integer imageId, String imageName, Integer size, String uploadTime, String contentType, String path, String md5) {
        this.imageId = imageId;
        this.imageName = imageName;
        this.size = size;
        this.uploadTime = uploadTime;
        this.contentType = contentType;
        this.path = path;
        this.md5 = md5;
    }
}
