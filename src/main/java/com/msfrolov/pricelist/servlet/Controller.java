package com.msfrolov.pricelist.servlet;

import com.msfrolov.pricelist.action.Action;
import com.msfrolov.pricelist.action.ActionFactory;
import com.msfrolov.pricelist.action.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private static final String URL_PATTERN = "/do/";
    private static final String PATH_PREFIX = "/WEB-INF/jsp/";
    private static final String PATH_POSTFIX = ".jsp";
    private ActionFactory actionFactory;

    @Override public void init() throws ServletException {
        actionFactory = new ActionFactory();
    }

    @Override public void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String actionName = req.getMethod() + req.getPathInfo();
        log.debug("action name: {}", actionName);
        Action action = actionFactory.getAction(actionName);
        log.debug("action: {}", action);
        if (checkAction(resp, action))
            return;
        ActionResult result = action.execute(req, resp);
        doForwardOrRedirect(result, req, resp);
    }

    private boolean checkAction(HttpServletResponse resp, Action action) throws IOException {
        boolean check = false;
        if (action == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
            check = true;
        }
        return check;
    }

    private void doForwardOrRedirect(ActionResult result, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (result.isRedirect()) {
            String location = req.getContextPath() + URL_PATTERN + result.getView();
            log.debug("redirect {}", location);
            resp.sendRedirect(location);
        } else {
            String path = PATH_PREFIX + result.getView() + PATH_POSTFIX;
            log.debug("forward {}", path);
            req.getRequestDispatcher(path).forward(req, resp);
        }
    }
}
