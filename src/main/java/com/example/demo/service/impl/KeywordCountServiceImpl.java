package com.example.demo.service.impl;

import com.example.demo.service.KeywordCountService;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
//        SparkSession spark = SparkSession.builder().appName("E-BookStore Keyword Count").getOrCreate();
//
//        Dataset<String> textData = spark.read().textFile(filePath + "/*.txt").cache();
//
//        Map<String, Long> keywordCounts = keywords.stream()
//                .collect(Collectors.toMap(keyword -> keyword, keyword ->
//                        textData.filter((FilterFunction<String>) line -> line.contains(keyword)).count()));
//
//        spark.stop();
//        return keywordCounts;
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("galaxy", 1L);
        return map;
    }
}
