package com.scorezone.scorezone.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
public class NewsService {

    @Value("${newsapi.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public NewsService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public List<Map<String, String>> fetchFootballNews() {
        List<Map<String, String>> articleList = new ArrayList<>();

        String url = "https://newsapi.org/v2/everything?q=(european football OR UEFA OR Champions League)&language=en&sortBy=publishedAt&apiKey=" + apiKey;

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode articles = root.path("articles");

            for (JsonNode article : articles) {
                Map<String, String> articleMap = new java.util.HashMap<>();
                articleMap.put("title", article.path("title").asText());
                articleMap.put("url", article.path("url").asText());
                articleMap.put("source", article.path("source").path("name").asText());

                if (!article.path("description").isNull()) {
                    articleMap.put("description", article.path("description").asText());
                }

                if (!article.path("urlToImage").isNull()) {
                    articleMap.put("image", article.path("urlToImage").asText());
                }

                articleList.add(articleMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Articles found: " + articleList.size());
        return articleList;
    }


}
