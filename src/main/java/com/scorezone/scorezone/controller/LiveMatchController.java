package com.scorezone.scorezone.controller;

import com.scorezone.scorezone.model.Match;
import com.scorezone.scorezone.service.LiveMatchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LiveMatchController {

    private final LiveMatchService liveMatchService;

    public LiveMatchController(LiveMatchService liveMatchService) {
        this.liveMatchService = liveMatchService;
    }

    @GetMapping("/live")
    public String showMatches(Model model) {


        List<Match> liveMatches = liveMatchService.fetchLiveMatches();
        List<Match> finishedMatches = liveMatchService.fetchFinishedMatches();
        List<Match> upcomingMatches = liveMatchService.fetchUpcomingMatches();


        System.out.println("LIVE: " + liveMatches.size());
        System.out.println("FINISHED: " + finishedMatches.size());
        System.out.println("UPCOMING: " + upcomingMatches.size());


        model.addAttribute("liveMatches", liveMatches);

        model.addAttribute("finishedMatches", finishedMatches);

        model.addAttribute("upcomingMatches", upcomingMatches);

        return "live";
    }

    @GetMapping("/match/{fixtureId}/events")
    public String showMatchEvents(@org.springframework.web.bind.annotation.PathVariable Long fixtureId, Model model) {
        var events = liveMatchService.fetchMatchEvents(fixtureId);
        System.out.println("EVENTS: " + events.size());
        model.addAttribute("events", events);
        return "events";
    }


}
