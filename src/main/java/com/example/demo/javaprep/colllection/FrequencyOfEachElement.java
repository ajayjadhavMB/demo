package com.example.demo.javaprep.colllection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrequencyOfEachElement {
    public static void main(String[] args) {
        List<String> list= new ArrayList<>(Arrays.asList("a", "a", "b", "c", "d", "c", "D", "e", "e"));
        

        Map<String, Integer> frequencyMap =new HashMap<>()  ;

        for (String element : list) {
           frequencyMap.put(element, frequencyMap.getOrDefault(element, 0) + 1);
        }

        System.out.println("Frequency of each element: " + frequencyMap);
    
    }
}