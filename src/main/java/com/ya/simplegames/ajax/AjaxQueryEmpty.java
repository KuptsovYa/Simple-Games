package com.ya.simplegames.ajax;

import com.ya.simplegames.config.SqlSafeUtil;

public class AjaxQueryEmpty {

    String unused;

    public String getUnused() { return unused; }

    public void setUnused(String unused) { this.unused = SqlSafeUtil.fix(unused); }
}