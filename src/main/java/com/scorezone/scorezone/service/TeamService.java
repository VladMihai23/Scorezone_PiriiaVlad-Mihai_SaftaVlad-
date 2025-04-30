package com.scorezone.scorezone.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

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

    private final Map<Integer, String> leagueNamesById = importantLeagues
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

    public Map<String, List<JsonNode>> fetchTeamsFromImportantLeagues() {
        Map<String, List<JsonNode>> teamsByLeague = new LinkedHashMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        for (Map.Entry<String, Integer> entry : importantLeagues.entrySet()) {
            String leagueName = entry.getKey();
            Integer leagueId = entry.getValue();
            String url = "https://v3.football.api-sports.io/standings?league=" + leagueId + "&season=2022";

            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode standingsArray = root.path("response").get(0).path("league").path("standings").get(0);

                List<JsonNode> teamList = new ArrayList<>();
                if (standingsArray.isArray()) {
                    for (JsonNode teamNode : standingsArray) {

                        ((ObjectNode) teamNode).putObject("league").put("id", leagueId);
                        teamList.add(teamNode);
                    }
                }

                teamsByLeague.put(leagueName, teamList);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return teamsByLeague;
    }

    public Map<String, Object> fetchTeamStatistics(int teamId, int leagueId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = "https://v3.football.api-sports.io/teams/statistics?team=" + teamId + "&league=" + leagueId + "&season=2023";

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode stats = root.path("response");

            Map<String, Object> result = new HashMap<>();
            result.put("played", stats.path("fixtures").path("played").path("total").asInt());
            result.put("wins", stats.path("fixtures").path("wins").path("total").asInt());
            result.put("draws", stats.path("fixtures").path("draws").path("total").asInt());
            result.put("losses", stats.path("fixtures").path("loses").path("total").asInt());
            result.put("goalsFor", stats.path("goals").path("for").path("total").path("total").asInt());
            result.put("goalsAgainst", stats.path("goals").path("against").path("total").path("total").asInt());

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public List<Map<String, Object>> fetchPlayersByTeam(int teamId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = "https://v3.football.api-sports.io/players?team=" + teamId + "&season=2022";

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode responseArray = root.path("response");

            List<Map<String, Object>> players = new ArrayList<>();

            for (JsonNode entry : responseArray) {
                JsonNode playerInfo = entry.path("player");
                JsonNode stats = entry.path("statistics").isArray() && entry.path("statistics").size() > 0
                        ? entry.path("statistics").get(0)
                        : null;

                Map<String, Object> player = new HashMap<>();

                player.put("id", playerInfo.path("id").asInt());
                player.put("name", playerInfo.path("name").asText());
                player.put("position", stats != null ? stats.path("games").path("position").asText("-") : "-");
                player.put("number", stats != null && !stats.path("games").path("number").isMissingNode()
                        ? stats.path("games").path("number").asText()
                        : "-");

                players.add(player);
            }

            return players;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public JsonNode fetchPlayerStatistics(int playerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = "https://v3.football.api-sports.io/players?id=" + playerId + "&season=2022";

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("response").get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




}
