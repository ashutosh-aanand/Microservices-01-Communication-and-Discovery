package com.learn.movieinfoservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieAPIResponse {
    @JsonProperty("Title")
    private String title;

    @JsonProperty("Plot")
    private String desc;

    public MovieAPIResponse(){}

    public MovieAPIResponse(String title, String plot) {
        this.title = title;
        desc = plot;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
