package com.learn.moviecatalogservice.resources;

import com.learn.moviecatalogservice.models.CatalogItem;
import com.learn.moviecatalogservice.models.MovieResponse;
import com.learn.moviecatalogservice.models.RatingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController // to use this class to handle routes
@RequestMapping("/catalog")
public class CatalogResource {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WebClient.Builder webClientBuilder;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        // get all rated movies from ratings data service -> it will have movie ids
        // creating a dummy list for now
        List<RatingResponse> ratings = Arrays.asList(
                new RatingResponse("1234", 4),
                new RatingResponse("4329", 5)
        );

        // for each movie id in the ratings, call movie info service and get details
        // add movie info and rating to Catalog item and return response
        // as a list of Catalog item (adding dummy data for now)

        List<CatalogItem> response = ratings.stream().map(rating -> {
            String movieId = rating.getMovieId();
//            MovieResponse movieResponse = restTemplate.getForObject("http://localhost:8083/movies/" + movieId, MovieResponse.class);

            MovieResponse movieResponse = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8083/movies/" + movieId)
                    .retrieve()
                    .bodyToMono(MovieResponse.class)
                    .block();

            return new CatalogItem(movieResponse.getName(), "sample description", rating.getRating());
        }).collect(Collectors.toList());

        return response;
    }
}

/*
new things:
* Collections.singletonList()
* "RestTemplate" fetches the response as json string which needs to be converted to an object
so, we are passing a class (having same keys as sent from the called service) as 2nd argument
- but there is an error here, the class passed must have "an empty constructor", so lets create it.

Things to fix:
- hardcoded url
- new restTemplate getting created for every request
    => create a single instance of RestTemplate class and use it whenever required
    => create its Bean and Autowire it where required [done]
-

* webclient
- async
- no waiting => non-blocking process
- here lets see how to use webclient to make the same request
- but in a blocking way, since our response is not a Mono stream,
- so even if we make the api call async, it will still behave as blocking.
- still, lets use it.
 */