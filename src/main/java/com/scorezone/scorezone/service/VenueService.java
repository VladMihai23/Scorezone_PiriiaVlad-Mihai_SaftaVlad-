package com.scorezone.scorezone.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scorezone.scorezone.model.Venue;
import com.scorezone.scorezone.repository.VenueRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class VenueService {

    @Value("${football.api.token}")
    private String apiToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @PostConstruct
    public void initAndSaveVenues() {
        List<Map<String, Object>> venuesFromApi = fetchAllVenuesFromApi();
        for (Map<String, Object> v : venuesFromApi) {
            String name = (String) v.get("name");
            if (venueRepository.findByName(name).isEmpty()) {
                Venue venue = new Venue();
                venue.setName(name);
                venue.setCity((String) v.get("city"));
                venue.setCapacity((Integer) v.get("capacity"));
                venueRepository.save(venue);
            }
        }
        System.out.println("Saved venues to DB if not already present.");
    }

    public List<Map<String, Object>> fetchAllVenuesFromApi() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", apiToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = "https://v3.football.api-sports.io/venues?search=stadium";

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode responseArray = root.path("response");

            List<Map<String, Object>> venues = new ArrayList<>();
            for (JsonNode venueNode : responseArray) {
                Map<String, Object> venue = new HashMap<>();
                venue.put("name", venueNode.path("name").asText());
                venue.put("city", venueNode.path("city").asText());
                venue.put("capacity", venueNode.path("capacity").asInt());
                venues.add(venue);
            }
            return venues;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
