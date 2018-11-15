package com.ya.simplegames.entity;

import com.ya.simplegames.config.SQLInjectionSafe;

import javax.persistence.*;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idGames")
    private Long idGames;

    @SQLInjectionSafe
    @Column(name = "gameName")
    private String gameName;

    public Game() {
    }

    public Game(String gameName) {
        this.gameName = gameName;
    }

    public Long getIdGames() {
        return idGames;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}