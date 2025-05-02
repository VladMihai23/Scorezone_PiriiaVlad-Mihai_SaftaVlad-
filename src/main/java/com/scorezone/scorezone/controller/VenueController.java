package com.scorezone.scorezone.controller;


import com.scorezone.scorezone.model.Venue;
import com.scorezone.scorezone.repository.VenueRepository;
import org.springframework.ui.Model;
import com.scorezone.scorezone.service.VenueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class VenueController {

    private final VenueService venueService;
    private final VenueRepository venueRepository;

    public VenueController(VenueService venueService, VenueRepository venueRepository) {
        this.venueService = venueService;
        this.venueRepository = venueRepository;

    }

    @GetMapping("/venues")
    public String showVenues(Model model) {

        venueService.initAndSaveVenues();

        model.addAttribute("allVenues", venueService.getAllVenues());

        model.addAttribute("venue", new Venue());
        model.addAttribute("error", null);

        return "venues";
    }



    @GetMapping("/venues/edit/{id}")
    public String editVenue(@PathVariable Long id, Model model) {
        var venue = venueService.getVenueById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid venue Id:" + id));
        model.addAttribute("venue", venue);
        model.addAttribute("apiVenues", venueService.fetchAllVenuesFromApi());
        model.addAttribute("localVenues", venueService.getAllLocalVenues());
        return "venues";
    }

    @PostMapping("/venues/save")
    public String saveVenue(@ModelAttribute("venue") Venue venue, Model model) {
        try {
            if (venue.getId() == null) {
                venueService.addVenue(venue);
            } else {
                venueService.updateVenue(venue.getId(), venue);
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("venue", venue);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("apiVenues", venueService.fetchAllVenuesFromApi());
            model.addAttribute("localVenues", venueService.getAllLocalVenues());
            return "venues";
        }

        return "redirect:/venues";
    }



    @GetMapping("/manage-venues")
    public String manageVenues(Model model) {
        model.addAttribute("localVenues", venueService.getAllLocalVenues());
        return "manage-venues";
    }







}
