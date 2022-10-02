package com.learn.moviecatalogservice.resources;

import com.learn.moviecatalogservice.models.CatalogItem;
import com.learn.moviecatalogservice.models.MovieResponse;
import com.learn.moviecatalogservice.models.RatingResponse;
import com.learn.moviecatalogservice.models.UserRatingResponse;
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

//    @Autowired
//    WebClient.Builder webClientBuilder;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        // get all rated movies from ratings data service -> it will have movie ids
        UserRatingResponse ratings = restTemplate.getForObject("http://ratings-data-service/ratings/users/" + userId, UserRatingResponse.class);

        // for each movie id in the ratings, call movie info service and get details
        List<CatalogItem> response = ratings.getUserRating().stream().map(rating -> {
            String movieId = rating.getMovieId();
            MovieResponse movieResponse = restTemplate.getForObject("http://movie-info-service/movies/" + movieId, MovieResponse.class);

            // add movie info and rating to Catalog item and create a list of CatalogItem
            return new CatalogItem(movieResponse.getName(), "sample description", rating.getRating());
        }).collect(Collectors.toList());

        // return response as a list of CatalogItem objects
        return response;
    }
}

/*
new things:
* Collections.singletonList()
* "RestTemplate" fetches the response as json string which needs to be converted to an object
so, we are passing a class (having same keys as sent from the called service) as 2nd argument
- but there is an error here, the class passed "must have an empty constructor", so lets create it.

Things to fix:
- hardcoded url
- new restTemplate getting created for every request
    => create a single instance of RestTemplate class and use it whenever required
    => create its Bean and Autowire it where required [done]
- our API returning a list rather than the list wrapped in an object.

-

* webclient
- async
- no waiting => non-blocking process
- here lets see how to use webclient to make the same request
- but in a blocking way, since our response is not a Mono stream,
- so even if we make the api call async, it will still behave as blocking.
- still, lets use it.
MovieResponse movieResponse = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8083/movies/" + movieId)
                    .retrieve()
                    .bodyToMono(MovieResponse.class)
                    .block();

* RestTemplate:
* to deserialise and consume a list of type RatingResponse, we had to pass "RatingResponse[].class"
* but now we will deserialise an object of class UserRatingResponse so passed UserRatingResponse.class

* Are both the api calls async ?
    -> NO, they are sync (blocking)
    -> to make them async, we need to
        - use reactive programming
        - use webClient for making the requests
        - return a stream (Mono or Flux) as a response

* If this microservice is being accessed in a multi-threaded env. do we need to handle any concurrency issues ?
    - when we deploy an app in servlet container, there are a lot of threads that run in ||el
    - each request spawns a new thread, and its important for all the objects to be thread-safe
    - and RestTemplate is "threadsafe"
    - one call doesn't affect another call

* When to use restTemplate and webClient ?
    - use webClient for all cases. It is flexible
    - can be used in both blocking and non-blocking way.

* Is it possible to make these calls to an external api that is not a microservice ?
    - Yes,
    - as long the source is producing json payload, it doesn't matter who is producing it
      and how it is being produced
    - it can be consumed in the same way

* How to handle security when communicating between microservices ?
  - many ways
    - using https
    - using authentication, we can have the rest call authenticated
        - eg., by putting the credentials in the header

-----------------------------------------------------------------------
## What are we doing wrong ?
- Hardcoding urls

### Why Hardcoded urls are bad ?
* as
    - changes require code updates
    - dynamic urls in the cloud -> need to accomodate to change
    - load balancing
    - Multiple environments -> local, staging, pre-prod/beta, canary, prod
        -> app needs to be aware of the environment

 As a solution to above, we have concept of Service Discovery.

# Service Discovery
-------------------
-> defines how the microservices know what to talk to.
-> its an abstraction layer(discovery server) between the consumer and producer microservices,
    which gives the url of the producer service.
- 2 types:
    - client side service discovery
    - server side service discovery

- Spring cloud uses client side service discovery.


 */