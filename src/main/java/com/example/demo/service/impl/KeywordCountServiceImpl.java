package com.example.demo.service.impl;

import com.example.demo.service.KeywordCountService;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        JavaRDD<String> textData = sc.textFile(filePath + "/*.txt").cache();

        Map<String, Long> keywordCounts = new HashMap<>();
        for (String keyword : keywords) {
            long count = textData.filter((Function<String, Boolean>) line -> line.contains(keyword)).count();
            keywordCounts.put(keyword, count);
        }

        sc.close();
        return keywordCounts;
    }
}
