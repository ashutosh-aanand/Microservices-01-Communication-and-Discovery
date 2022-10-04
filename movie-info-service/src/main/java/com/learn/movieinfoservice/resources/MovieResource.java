package com.learn.movieinfoservice.resources;

import com.learn.movieinfoservice.models.Movie;
import com.learn.movieinfoservice.models.MovieAPIResponse;
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

    @Value("${omdbapi.key}")
    private String apiKey;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId){

        MovieAPIResponse movieAPIResponse =
                restTemplate.getForObject("http://www.omdbapi.com/?apikey="+ apiKey +"&i=" + movieId, MovieAPIResponse.class);

        return new Movie(movieId, movieAPIResponse.getTitle(), movieAPIResponse.getDesc());
    }
}
