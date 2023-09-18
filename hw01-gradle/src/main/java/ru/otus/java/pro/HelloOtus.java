package ru.otus.java.pro;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;

import java.util.stream.Collectors;

public class HelloOtus {
    static public void main(String [] args) {
        String word = "testword";
        Multiset<String> character_counter = HashMultiset.create();
        character_counter.addAll(
                Lists.charactersOf(word)
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.toList())
        );
    }
}
