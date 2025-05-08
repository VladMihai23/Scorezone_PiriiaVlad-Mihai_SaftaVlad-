package com.scorezone.scorezone.model;


import jakarta.persistence.*;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    private String name;

    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Player() {}

    public Player(String name, String position, Team team) {
        this.name = name;
        this.position = position;
        this.team = team;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }



}
