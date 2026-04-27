package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/stream")
public class JavaStreamController {

    @GetMapping("/mapVsFlatMap")
    public String getMapVsFlatMapDifference(@RequestParam String param) {
        // map
List<String> names = List.of("a", "b");
names.stream().map(String::toUpperCase);

// flatMap
List<List<String>> list = List.of(List.of("a"), List.of("b"));
list.stream().flatMap(List::stream);


        return "Map vs FlatMap difference explained in console logs.";

    }
    
    
}
