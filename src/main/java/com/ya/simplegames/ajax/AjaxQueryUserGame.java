package com.ya.simplegames.ajax;

import com.ya.simplegames.config.SqlSafeUtil;

public class AjaxQueryUserGame {

    Long userId;

    Long gameId;

    String word;

    public String getWord() { return word; }

    public void setWord(String word) { this.word = SqlSafeUtil.fix(word); }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
