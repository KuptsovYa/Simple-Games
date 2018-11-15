package com.ya.simplegames.ajax;

import com.ya.simplegames.config.SqlSafeUtil;

public class AjaxQueryPersonal {

    Long userId;

    String firstName;

    String secondName;

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = SqlSafeUtil.fix(firstName); }

    public String getSecondName() { return secondName; }

    public void setSecondName(String secondName) { this.secondName = SqlSafeUtil.fix(secondName); }
}
