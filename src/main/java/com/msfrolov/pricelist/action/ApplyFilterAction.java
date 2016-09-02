package com.msfrolov.pricelist.action;

import com.msfrolov.pricelist.exception.PriceException;
import com.msfrolov.pricelist.model.Product;
import com.msfrolov.pricelist.service.PriceService;
import com.msfrolov.pricelist.service.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplyFilterAction implements Action {

    private static final Logger log = LoggerFactory.getLogger(ApplyFilterAction.class);
    private ActionResult result = new ActionResult("price-list");
    private ServiceFactory serviceFactory = new ServiceFactory();

    public ApplyFilterAction() {
    }

    @Override public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
        log.debug("filter action");
        Map<String, Object> filterMap = new HashMap<>();
        addStringFiler(request, "category", filterMap);
        addStringFiler(request, "name", filterMap);
        addNumberFiler(request, "price_from", filterMap);
        addNumberFiler(request, "price_to", filterMap);

        PriceService service = serviceFactory.getService("PriceService", PriceService.class);
        List<Product> products = service.findByFilters(filterMap, Product.class);
        request.setAttribute("products", products);
        return result;
    }

    private void addStringFiler(HttpServletRequest request, String filterName, Map<String, Object> filterMap) {
        log.debug("add string filter: {}", filterName);
        String filter = request.getParameter(filterName);
        if (filter != null && !filter.isEmpty()) {
            filterMap.put(filterName, filter);
        }
    }

    private void addNumberFiler(HttpServletRequest request, String filterName, Map<String, Object> filterMap) {
        log.debug("add number filter: {}", filterName);
        String filter = request.getParameter(filterName);
        if (filter != null && !filter.isEmpty()) {
            BigDecimal filterNumber;
            try {
                filterNumber = new BigDecimal(filter);
            }catch (Exception e){
                throw new PriceException("failed to convert string in number", e);
            }
            filterMap.put(filterName, filterNumber);
        }
    }
}
