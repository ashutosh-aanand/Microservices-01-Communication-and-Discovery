package com.learn.moviecatalogservice.models;

import java.util.List;

public class UserRatingResponse {
    private List<RatingResponse> userRating;

    public UserRatingResponse(){}

    public UserRatingResponse(List<RatingResponse> userRating) {
        this.userRating = userRating;
    }

    public List<RatingResponse> getUserRating() {
        return userRating;
    }

    public void setUserRating(List<RatingResponse> userRating) {
        this.userRating = userRating;
    }
}
