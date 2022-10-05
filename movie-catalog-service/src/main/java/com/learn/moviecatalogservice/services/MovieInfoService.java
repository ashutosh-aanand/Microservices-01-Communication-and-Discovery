package com.learn.moviecatalogservice.services;

import com.learn.moviecatalogservice.models.CatalogItem;
import com.learn.moviecatalogservice.models.MovieResponse;
import com.learn.moviecatalogservice.models.RatingResponse;
import com.learn.moviecatalogservice.resources.CatalogResource;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
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

    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem",
            // if method execution fails {timeout OR any error } -> use the fallback method instead

            commandProperties = {
                    // the timeout for this method => if times out -> counted as a failed request
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
                    // number of last requests to observe
                    @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value = "6"),
                    // % of request failed in the observed list of request
                    // fail => {timeouts OR any error thrown in the method execution }
                    @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value = "50"),
                    // how long to break the circuit before trying again (meanwhile send fallbacks)
                    @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value = "15000")
            },

    // Implementing Bulkhead pattern
            threadPoolKey = "movieInfoPool",
            threadPoolProperties = {
                    @HystrixProperty(name= "coreSize", value = "15"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            }
    )
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
/*
* tested if configuring is working or not
* by stopping the movie-info-service app, then making more than 3 requests (which failed, as the service is down)
* The circuit breaker was open, and it was sending fallback for this service for any request made in 15 seconds.
* Also started movie-info-service in midway of 15 sec, still the circuit breaker was open => so was getting fallbacks.
* After 15 seconds => started getting actual response.
* Working as expected !!
*
 */