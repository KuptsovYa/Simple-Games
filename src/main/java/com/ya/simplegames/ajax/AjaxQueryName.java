package com.ya.simplegames.ajax;

import com.ya.simplegames.config.SqlSafeUtil;

public class AjaxQueryName {

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = SqlSafeUtil.fix(name);
    }
}