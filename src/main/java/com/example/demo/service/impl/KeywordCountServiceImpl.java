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

        JavaRDD<String> lines = sc.textFile(filePath + "/*.txt")
                .map(line -> line.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", " "));

        JavaRDD<String> words = lines.mapPartitionsWithIndex((index, iterator) -> {
            List<String> results = new ArrayList<>();
            String previousLineEnd = "";
            while (iterator.hasNext()) {
                String currentLine = iterator.next();
                if (!previousLineEnd.isEmpty()) {
                    results.add(previousLineEnd + currentLine);
                }
                String[] currentWords = currentLine.split("\\s+");
                Collections.addAll(results, currentWords);

                if (currentWords.length > 0) {
                    previousLineEnd = currentWords[currentWords.length - 1];
                }
            }
            return results.iterator();
        }, true);

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
