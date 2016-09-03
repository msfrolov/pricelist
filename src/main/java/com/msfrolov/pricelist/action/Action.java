package com.msfrolov.pricelist.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Front Controller delegates requests for the implementation of this interface
 */
public interface Action {

    /**
     * Method for the logic and services
     *
     */
    public ActionResult execute(HttpServletRequest req, HttpServletResponse resp);
}
