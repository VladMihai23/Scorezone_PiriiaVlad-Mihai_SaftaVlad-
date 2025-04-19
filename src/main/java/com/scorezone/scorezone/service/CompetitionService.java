package com.scorezone.scorezone.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompetitionService {

    @Value("${football.api.token}")
    private String apiToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<JsonNode> fetchAllCompetitions() {
        String url = "https://v3.football.api-sports.io/leagues";
        return fetchCompetitionsFromAPI(url);
    }

    public List<JsonNode> searchCompetitionsByName(String name) {
        String url = "https://v3.football.api-sports.io/leagues?search=" + name;
        return fetchCompetitionsFromAPI(url);
    }

    private List<JsonNode> fetchCompetitionsFromAPI(String url) {
        List<JsonNode> list = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            for (JsonNode league : root.path("response")) {
                list.add(league);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
