package com.msfrolov.pricelist.action;

/**
 * ActionResult class that contains information about the result
 * of data processing action and further instructions
 * as a path to the view or address for redirect
 */
public class ActionResult {
    private final String view;
    private final boolean redirect;

    public ActionResult(String page, boolean redirect) {
        this.view = page;
        this.redirect = redirect;
    }

    public ActionResult(String page) {
        this.view = page;
        this.redirect = false;
    }

    public String getView() {
        return view;
    }

    public boolean isRedirect() {
        return redirect;
    }
}
