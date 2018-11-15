package com.ya.simplegames.ajax;

import com.ya.simplegames.config.SqlSafeUtil;

public class AjaxQueryChatSendMsg {

    String message;

    Long userId;

    Long lobbyId;

    public Long getLobbyId() { return lobbyId; }

    public void setLobbyId(Long lobbyId) { this.lobbyId = lobbyId; }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = SqlSafeUtil.fix(message); }
}
