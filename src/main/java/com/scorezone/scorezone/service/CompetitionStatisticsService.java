package com.scorezone.scorezone.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompetitionStatisticsService {

    @Value("${football.api.token}")
    private String apiToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, Integer> importantLeagues = Map.of(
            "Premier League", 39,
            "La Liga", 140,
            "Serie A", 135,
            "Bundesliga", 78,
            "Ligue 1", 61
    );

    public List<Map<String, Object>> fetchCompetitionStatistics() {
        List<Map<String, Object>> statisticsList = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        int season = 2023;

        for (Map.Entry<String, Integer> entry : importantLeagues.entrySet()) {
            String leagueName = entry.getKey();
            Integer leagueId = entry.getValue();
            String url = "https://v3.football.api-sports.io/fixtures?league=" + leagueId + "&season=" + season;

            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode fixtures = root.path("response");

                int totalMatches = fixtures.size();
                int totalGoals = 0;

                for (JsonNode match : fixtures) {
                    int homeGoals = match.path("goals").path("home").asInt(0);
                    int awayGoals = match.path("goals").path("away").asInt(0);
                    totalGoals += homeGoals + awayGoals;
                }

                Map<String, Object> leagueStats = new HashMap<>();
                leagueStats.put("leagueName", leagueName);
                leagueStats.put("totalMatches", totalMatches);
                leagueStats.put("totalGoals", totalGoals);

                statisticsList.add(leagueStats);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return statisticsList;
    }

}
