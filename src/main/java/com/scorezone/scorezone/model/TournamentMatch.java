package com.scorezone.scorezone.model;


import jakarta.persistence.*;


@Entity
public class TournamentMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tournamentmatch_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team1_id")
    private Team team1;

    @ManyToOne
    @JoinColumn(name = "team2_id")
    private Team team2;

    private int team1Score;
    private int team2Score;

    private String status;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    public TournamentMatch() {}

    public Long getId() {
        return id;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(int team1Score) {
        this.team1Score = team1Score;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(int team2Score) {
        this.team2Score = team2Score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Venue getVenue() {
        return venue;
    }
    public void setVenue(Venue venue) {
        this.venue = venue;
    }
}
