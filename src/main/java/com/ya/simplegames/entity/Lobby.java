package com.ya.simplegames.entity;

import com.ya.simplegames.config.SQLInjectionSafe;

import javax.persistence.*;

@Entity
@Table(name = "lobby")
public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLobby")
    private Long idLobby;

    @Column(name = "game")
    private Long game;

    @SQLInjectionSafe
    @Column(name = "gameLog")
    private String gameLog;

    @Column(name = "player1")
    private Long player1;

    @Column(name = "player2")
    private Long player2;

    public Lobby() {
    }

    public Lobby(Long game, String gameLog, Long player1, Long player2) {

        this.game = game;
        this.gameLog = gameLog;
        this.player1 = player1;
        this.player2 = player2;
    }

    public Long getIdLobby() { return idLobby; }

    public Long getGame() { return game; }

    public void setGame(Long game) { this.game = game; }

    public String getGameLog() { return gameLog; }

    public void setGameLog(String gameLog) { this.gameLog = gameLog; }

    public Long getPlayer1() { return player1; }

    public void setPlayer1(Long player1) { this.player1 = player1; }

    public Long getPlayer2() { return player2; }

    public void setPlayer2(Long player2) { this.player2 = player2; }
}