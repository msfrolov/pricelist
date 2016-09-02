package com.msfrolov.pricelist.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp);
}
