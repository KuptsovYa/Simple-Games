package com.ya.simplegames.entity;

import com.ya.simplegames.config.SQLInjectionSafe;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idChat")
    private Long idChat;

    @SQLInjectionSafe
    @Column(name = "lobbyId")
    private String lobbyId;

    @Column(name = "player")
    private Long player;

    @SQLInjectionSafe
    @Column(name = "message")
    private String message;

    @Column(name = "timeMark")
    private Timestamp timeMark;

    public Chat() {
    }

    public Chat(String lobbyId, Long player, String message, Timestamp timeMark) {
        this.lobbyId = lobbyId;
        this.player = player;
        this.message = message;
        this.timeMark = timeMark;
    }

    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    public Long getPlayer() {
        return player;
    }

    public void setPlayer(Long player) {
        this.player = player;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimeMark() {
        return timeMark;
    }

    public void setTimeMark(Timestamp timeMark) {
        this.timeMark = timeMark;
    }
}
