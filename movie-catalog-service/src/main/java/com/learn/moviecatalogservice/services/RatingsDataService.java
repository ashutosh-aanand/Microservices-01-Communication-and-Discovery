package com.learn.moviecatalogservice.services;

import com.learn.moviecatalogservice.models.RatingResponse;
import com.learn.moviecatalogservice.models.UserRatingResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class RatingsDataService {

    Logger log = LoggerFactory.getLogger(RatingsDataService.class);

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackUserRatings")
    public UserRatingResponse getUserRatings(String userId) {
        log.info("sending request to ratings-data-service !");
        UserRatingResponse ratings = restTemplate.getForObject("http://ratings-data-service/ratings/users/" + userId, UserRatingResponse.class);
        log.info("received ratings-data-service response: {}", ratings);
        return ratings;
    }

    private UserRatingResponse getFallbackUserRatings(String userId) {
        UserRatingResponse fallbackResponse = new UserRatingResponse();
        fallbackResponse.setUserRating(Arrays.asList(new RatingResponse("fallback-movie-id", 0)));
        log.info("Returning fallback-user-ratings: {}", fallbackResponse);
        return fallbackResponse;
    }

}
