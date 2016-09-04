package com.msfrolov.pricelist.action;

import com.msfrolov.pricelist.model.Product;
import com.msfrolov.pricelist.service.PriceService;
import com.msfrolov.pricelist.service.ServiceFactory;
import com.msfrolov.pricelist.util.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ApplyFilterAction implements Action {

    private static final Logger log = LoggerFactory.getLogger(ApplyFilterAction.class);
    private ActionResult result = new ActionResult("price-list");
    private ServiceFactory serviceFactory = new ServiceFactory();

    public ApplyFilterAction() {
    }

    @Override public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
        log.debug("filter action");
        StringBuilder message = new StringBuilder();
        Map<String, Object> filterMap = new HashMap<>();
        addStringFiler("category", filterMap, request, message);
        addStringFiler("name", filterMap, request, message);
        addNumberFiler("price_from", filterMap, request, message);
        addNumberFiler("price_to", filterMap, request, message);
        PriceService service = serviceFactory.getService("PriceService", PriceService.class);
        List<Product> products = service.findByFilters(filterMap, Product.class);
        if (products == null) {
            Properties properties = PropertiesManager.getProperties("properties/message.properties");
            message.append(properties.getProperty("empty-filters"));
        }
        request.setAttribute("products", products);
        request.setAttribute("message", message);
        return result;
    }

    private void addStringFiler(final String filterName, Map<String, Object> filterMap, HttpServletRequest request,
            StringBuilder message) {
        log.debug("add string filter: {}", filterName);
        String filter = request.getParameter(filterName);
        request.setAttribute(filterName, filter);
        if (filter != null && !filter.isEmpty()) {
            log.debug("filterName: {}", filterName);
            log.debug("filterValue: {}", filter);
            filterMap.put(filterName, filter);
        }
    }

    private void addNumberFiler(final String filterName, Map<String, Object> filterMap, HttpServletRequest request,
            StringBuilder message) {
        log.debug("add number filter: {}", filterName);
        String filter = request.getParameter(filterName);
        request.setAttribute(filterName, filter);
        if (filter != null && !filter.isEmpty()) {
            BigDecimal filterNumber;
            try {
                filterNumber = new BigDecimal(filter);
                log.debug("filterName: {}", filterName);
                log.debug("filterValue: {}", filterNumber);
                filterMap.put(filterName, filterNumber);
            } catch (Exception e) {
                Properties properties = PropertiesManager.getProperties("properties/message.properties");
                message.append(properties.getProperty("failed-number-filter"));
            }
        }
    }
}
