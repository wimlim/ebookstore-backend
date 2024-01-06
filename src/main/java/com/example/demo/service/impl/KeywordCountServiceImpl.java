package com.example.demo.service.impl;

import com.example.demo.service.KeywordCountService;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class KeywordCountServiceImpl implements KeywordCountService {

    private final List<String> keywords = Arrays.asList(
            "galaxy", "space", "time", "scientist", "adventure",
            "psyche", "behavior", "emotions", "consciousness", "mind",
            "leader", "history", "dynasty", "era", "biography"
    );

    @Override
    public Map<String, Long> countKeywords(String filePath) {
        SparkConf conf = new SparkConf().setAppName("E-BookStore Keyword Count").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        String textContent = sc.textFile(filePath + "/*.txt")
                .map(line -> line.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", " "))
                .collect()
                .stream()
                .collect(Collectors.joining(" "));

        JavaRDD<String> words = sc.parallelize(Collections.singletonList(textContent))
                .flatMap(content -> Arrays.asList(content.split("\\s+")).iterator());

        Map<String, Long> keywordCounts = new HashMap<>();
        for (String keyword : keywords) {
            String pattern = "\\b" + keyword + "\\b";
            long count = words.filter(word -> word.matches(pattern)).count();
            keywordCounts.put(keyword, count);
        }

        sc.close();
        return keywordCounts;
    }


}
