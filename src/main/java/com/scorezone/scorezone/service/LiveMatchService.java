package com.scorezone.scorezone.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scorezone.scorezone.model.Match;
import com.scorezone.scorezone.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class LiveMatchService {

    private final MatchRepository matchRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${football.api.token}")
    private String apiToken;

    public LiveMatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public List<Match> fetchLiveMatches() {
        String url = "https://v3.football.api-sports.io/fixtures?live=all";
        return fetchMatches(url);
    }

    public List<Match> fetchFinishedMatches() {
        String today = LocalDate.now().toString();
        String url = "https://v3.football.api-sports.io/fixtures?date=" + today + "&status=FT";
        return fetchMatches(url);
    }

    public JsonNode fetchMatchEvents(Long fixtureId){
        String url = "https://v3.football.api-sports.io/fixtures/events?fixture=" + fixtureId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try{
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return objectMapper.readTree(response.getBody()).path("response");

        } catch (Exception e) {
            e.printStackTrace();
            return objectMapper.createArrayNode();
        }
    }

    public List<Match> fetchUpcomingMatches() {
        String today = LocalDate.now().toString();
        String url = "https://v3.football.api-sports.io/fixtures?date=" + today + "&status=NS";
        return fetchMatches(url);
    }

    private List<Match> fetchMatches(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        List<Match> matches = new ArrayList<>();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode fixtures = root.path("response");

            for (JsonNode f : fixtures) {
                Match match = new Match();
                Long fixtureId = f.path("fixture").path("id").asLong();

                match.setFixtureId(fixtureId);
                match.setLeague(f.path("league").path("name").asText());
                match.setHomeTeam(f.path("teams").path("home").path("name").asText());
                match.setAwayTeam(f.path("teams").path("away").path("name").asText());
                match.setHomeScore(f.path("goals").path("home").asInt());
                match.setAwayScore(f.path("goals").path("away").asInt());

                String status = f.path("fixture").path("status").path("short").asText();
                match.setStatus(status);

                String utcDate = f.path("fixture").path("date").asText();
                ZonedDateTime parsedDate = ZonedDateTime.parse(utcDate);
                String formatted = parsedDate
                        .withZoneSameInstant(ZoneId.of("Europe/Bucharest"))
                        .format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"));
                match.setTime(formatted);

                Optional<Match> existing = matchRepository.findByFixtureId(fixtureId);
                existing.ifPresent(value -> match.setId(value.getId()));
                matchRepository.save(match);


                matches.add(match);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return matches;
    }
}
