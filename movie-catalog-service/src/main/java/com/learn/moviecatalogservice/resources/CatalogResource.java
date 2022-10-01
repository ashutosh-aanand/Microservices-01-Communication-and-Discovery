package com.learn.moviecatalogservice.resources;

import com.learn.moviecatalogservice.models.CatalogItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController // to use this class to handle routes
@RequestMapping("/catalog")
public class CatalogResource {

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){

        // get all rated movies from ratings data service -> it will have movie ids
        // creating a dummy list for now

        // for each movie id call movie info service and get details
        // add movie info and rating to Catalog item and return response
        // as a list of Catalog item

        return Collections.singletonList(
                new CatalogItem("3 idiots", "engineering life", 5)
        );
    }
}

/*
new things:
* Collections.singletonList()
 */