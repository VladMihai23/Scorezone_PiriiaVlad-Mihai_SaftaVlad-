package com.scorezone.scorezone.model;


import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "matches")
public class Match {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long fixtureId;

    private String league;
    private String homeTeam;
    private String awayTeam;
    private int homeScore;
    private int awayScore;
    private String time;
    private String status;





    public String getLeague() {
        return league;
    }
    public void setLeague(String league) {
        this.league = league;
    }
    public String getHomeTeam() {
        return homeTeam;
    }
    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }
    public String getAwayTeam() {
        return awayTeam;
    }
    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }
    public int getHomeScore() {
        return homeScore;
    }
    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }
    public int getAwayScore() {
        return awayScore;
    }
    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getFixtureId() {
        return fixtureId;
    }
    public void setFixtureId(Long fixtureId) {
        this.fixtureId = fixtureId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }



}
