package com.learn.movieinfoservice.resources;

import com.learn.movieinfoservice.models.Movie;
import com.learn.movieinfoservice.models.MovieAPIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    Logger log = LoggerFactory.getLogger(MovieResource.class);

    @Value("${omdbapi.key}")
    private String apiKey;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId){

        log.info("Making API call for movieId: {}", movieId);
        MovieAPIResponse movieAPIResponse =
                restTemplate.getForObject("http://www.omdbapi.com/?apikey="+ apiKey +"&i=" + movieId, MovieAPIResponse.class);

        Movie response = new Movie(movieId, movieAPIResponse.getTitle(), movieAPIResponse.getDesc());

        log.info("Returning movie info response: {}", response);
        return response;
    }
}
