package com.learn.ratingsdataservice.resources;

import com.learn.ratingsdataservice.models.Rating;
import com.learn.ratingsdataservice.models.UserRating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingsResource {

    @GetMapping("/{movieId}")
    public Rating getMovieRating(@PathVariable("movieId") String movieId){
        return new Rating(movieId, 4);
    }

    @GetMapping("/users/{userId}")
    public UserRating getUserRatings(@PathVariable("userId") String userId){
        List<Rating> ratings = Arrays.asList(
                new Rating("1234", 4),
                new Rating("4329", 5),
                new Rating("8743", 3)
        );
        return new UserRating(ratings);
    }
}
/*
* Is it good to return a list as response ?
* -> No
* suppose we want to send few additional keys along with the list:
*   in the current setup, we won't be able to do it instantly,
*   we would have to let the consumers know that we have changed the class
*   definition and now will be sending this list as a key along with other keys
*
*   but if we had already sent the list wrapped in an object, we would just add the
*   new keys and send it => it would be backward compatible
*   -> we won't need to let the consumers know after the change, the consumers will
*   still receive the data they were receiving, plus if they want they can receive the new keys
*   But it won't break anything.
*
*   So, we should avoid returning list in API response directly,
*   rather wrap it in a object and then return it.
 */

