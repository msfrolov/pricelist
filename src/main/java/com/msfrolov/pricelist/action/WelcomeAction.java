package com.msfrolov.pricelist.action;

import com.msfrolov.pricelist.model.Product;
import com.msfrolov.pricelist.service.PriceService;
import com.msfrolov.pricelist.service.ServiceFactory;
import com.msfrolov.pricelist.util.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Properties;

public class WelcomeAction implements Action {

    private static final Logger log = LoggerFactory.getLogger(WelcomeAction.class);
    private ActionResult result = new ActionResult("price-list");
    private ServiceFactory serviceFactory = new ServiceFactory();

    public WelcomeAction() {
    }

    @Override public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
        log.debug("welcome action");
        PriceService service = serviceFactory.getService("PriceService", PriceService.class);
        List<Product> products = service.findAll(Product.class);
        request.setAttribute("products", products);
        Properties properties = PropertiesManager.getProperties("properties/message.properties");
        request.setAttribute("message", properties.getProperty("welcome"));
        return result;
    }
}
