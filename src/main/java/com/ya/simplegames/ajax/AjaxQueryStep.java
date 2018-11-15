package com.ya.simplegames.ajax;

import com.ya.simplegames.config.SqlSafeUtil;

public class AjaxQueryStep {

    String value;

    Long lobbyId;

    int playerNum;

    public String getValue() { return value; }

    public void setValue(String value) { this.value = SqlSafeUtil.fix(value); }

    public Long getLobbyId() { return lobbyId; }

    public void setLobbyId(Long lobbyId) { this.lobbyId = lobbyId; }

    public int getPlayerNum() { return playerNum; }

    public void setPlayerNum(int playerNum) { this.playerNum = playerNum; }
}