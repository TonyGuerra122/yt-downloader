package com.tonyguerra.ytdownloader.dto;

public final record Video(
        String url,
        String title,
        String author,
        float duration,
        String description,
        String thumbnail) {

}
