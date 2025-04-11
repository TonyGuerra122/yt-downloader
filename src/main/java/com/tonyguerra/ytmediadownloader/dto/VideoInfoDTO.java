package com.tonyguerra.ytmediadownloader.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VideoInfoDTO(
                @JsonProperty("title") String title,
                @JsonProperty("uploader") String author,
                @JsonProperty("duration") float duration,
                @JsonProperty("thumbnail") String thumbnail,
                @JsonProperty("description") String description,
                @JsonProperty("resolution") String resolution) {
}
