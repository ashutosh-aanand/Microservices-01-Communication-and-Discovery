package com.learn.moviecatalogservice.services;

import com.learn.moviecatalogservice.models.CatalogItem;
import com.learn.moviecatalogservice.models.MovieResponse;
import com.learn.moviecatalogservice.models.RatingResponse;
import com.learn.moviecatalogservice.resources.CatalogResource;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfoService {

    Logger log = LoggerFactory.getLogger(MovieInfoService.class);

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    public CatalogItem getCatalogItem(RatingResponse rating, String movieId) {
        log.info("sending request to movie-info-service for movieId: {}", movieId);
        MovieResponse movieResponse = restTemplate.getForObject("http://movie-info-service/movies/" + movieId, MovieResponse.class);
        log.info("received movie-info-service response: {}", movieResponse);

        // add movie info and rating to Catalog item and create a list of CatalogItem
        return new CatalogItem(movieResponse.getName(), movieResponse.getDesc(), rating.getRating());
    }

    private CatalogItem getFallbackCatalogItem(RatingResponse rating, String movieId) {
        CatalogItem fallbackResponse = new CatalogItem("fallback-movie-name", "fallback-desc", rating.getRating());
        log.info("Returning fallback-movie-info: {}", fallbackResponse);
        return fallbackResponse;
    }

}
