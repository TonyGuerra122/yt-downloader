package com.tonyguerra.ytmediadownloader.enums;

public enum MediaType {
    AUDIO("bestaudio"),
    VIDEO("bestvideo+bestaudio/best");

    private final String mediaFormat;

    MediaType(String mediaFormat) {
        this.mediaFormat = mediaFormat;
    }

    public String getMediaFormat() {
        return this.mediaFormat;
    }
}
