package com.scorezone.scorezone.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Service
public class TeamService {

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

    public Map<String, List<JsonNode>> fetchTeamsFromImportantLeagues() {
        Map<String, List<JsonNode>> teamsByLeague = new LinkedHashMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        for (Map.Entry<String, Integer> entry : importantLeagues.entrySet()) {
            String leagueName = entry.getKey();
            Integer leagueId = entry.getValue();
            String url = "https://v3.football.api-sports.io/standings?league=" + leagueId + "&season=2023";
            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode standingsArray = root.path("response").get(0).path("league").path("standings").get(0);

                List<JsonNode> teamList = new ArrayList<>();
                if (standingsArray.isArray()) {
                    standingsArray.forEach(teamList::add);
                }
                teamsByLeague.put(leagueName, teamList);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return teamsByLeague;
    }
}
