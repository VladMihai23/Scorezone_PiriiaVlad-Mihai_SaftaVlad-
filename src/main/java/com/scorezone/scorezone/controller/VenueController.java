package com.scorezone.scorezone.controller;


import org.springframework.ui.Model;
import com.scorezone.scorezone.service.VenueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping("/venues")
    public String showVenues(Model model) {
        var venues = venueService.fetchAllVenuesFromApi();
        model.addAttribute("venues", venues);

        System.out.println("Venues loaded: " + venues.size());

        return "venues";    }
}
