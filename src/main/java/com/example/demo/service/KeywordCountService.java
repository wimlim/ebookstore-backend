package com.example.demo.service;

import java.util.List;
import java.util.Map;

public interface KeywordCountService {
    Map<String, Long> countKeywords(String filePath);
}