package com.example.demo.javaprep.colllection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RemoveDuplicateElement {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<> (Arrays.asList(1,2,2, 4, 5, 6, 6, 7,9));

        Set<Integer> set = new LinkedHashSet<>(list);

        List<Integer> uniqeIntegers =  new ArrayList<>(set);

        System.out.println("unique inters: " +  uniqeIntegers);

    }
}