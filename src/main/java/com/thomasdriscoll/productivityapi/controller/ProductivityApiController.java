package com.thomasdriscoll.productivityapi.controller;

import com.thomasdriscoll.productivityapi.lib.exceptions.DriscollException;
import com.thomasdriscoll.productivityapi.lib.responses.DriscollResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductivityApiController {

    private ProductivityApiService productivityApiService;

    public ProductivityApiController(ProductivityApiService productivityApiService){
        this.productivityApiService = productivityApiService;
    }

    @GetMapping("/{name}")
    private ResponseEntity<DriscollResponse<String>> dummyFunction(@PathVariable String name) throws DriscollException {
        return ResponseEntity.ok().body(new DriscollResponse<>(HttpStatus.OK.value(), productivityApiService.dummyFunction(name)));
    }
}

