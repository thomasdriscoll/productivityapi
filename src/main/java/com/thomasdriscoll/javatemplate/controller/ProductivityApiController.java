package com.thomasdriscoll.javatemplate.controller;

import com.thomasdriscoll.javatemplate.lib.exceptions.DriscollException;
import com.thomasdriscoll.javatemplate.lib.responses.DriscollResponse;
import com.thomasdriscoll.javatemplate.service.ProductivityApiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

