package com.mehedicr.tourmate.Model;

import java.io.Serializable;

public class Moment implements Serializable {
    private String key;
    private String fileName;
    private String formatName;
    private String date;
    private String photoPath;

    public Moment() {
        //required for firebase
    }

    public Moment(String key, String fileName, String formatName, String date, String photoPath) {
        this.key = key;
        this.fileName = fileName;
        this.formatName = formatName;
        this.date = date;
        this.photoPath = photoPath;
    }

    public String getKey() {
        return key;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFormatName() {
        return formatName;
    }

    public String getDate() {
        return date;
    }

    public String getPhotoPath() {
        return photoPath;
    }
}
