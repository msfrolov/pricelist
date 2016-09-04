package com.msfrolov.pricelist.action;

import com.msfrolov.pricelist.model.Category;
import com.msfrolov.pricelist.model.Product;
import com.msfrolov.pricelist.service.PriceService;
import com.msfrolov.pricelist.service.PriceServiceImpl;
import com.msfrolov.pricelist.service.ServiceFactory;
import com.msfrolov.pricelist.util.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

public class WelcomeAction implements Action {

    private static final Logger log = LoggerFactory.getLogger(WelcomeAction.class);
    private ActionResult result = new ActionResult("price-list");
    private ServiceFactory serviceFactory = new ServiceFactory();

    public WelcomeAction() {
    }

    @Override public ActionResult execute(HttpServletRequest request, HttpServletResponse response) {
        log.debug("welcome action");
        initContent();
        PriceService service = serviceFactory.getService("PriceService", PriceService.class);
        List<Product> products = service.findAll(Product.class);
        request.setAttribute("products", products);
        Properties properties = PropertiesManager.getProperties("properties/message.properties");
        request.setAttribute("message", properties.getProperty("welcome"));
        return result;
    }


    private void initContent(){
        //given
        PriceService service = new PriceServiceImpl();
        List<Product> all = service.findAll(Product.class);
        //when
        if (all.isEmpty()) {
            List<String> garden = readFileToList("price/Monitor ProView.txt");
            List<String> dining = readFileToList("price/Notebook Acer.txt");
            List<String> living = readFileToList("price/Monitor Sony.txt");
            //then
            handleList("Садовая мебель", garden, service);
            handleList("Столовая мебель", dining, service);
            handleList("Гостинная мебель", living, service);
        }

    }

    private void handleList(String categoryName, List<String> productNames, PriceService service) {
        log.debug("try to add category - {}", categoryName);
        Random random = new Random();
        Category category = new Category();
        category.setName(categoryName);
        service.add(category);
        for (String s : productNames) {
            log.debug("try to add product - {}", s);
            Product product = new Product();
            product.setName(s);
            product.setCategory(category);
            product.setPrice(BigDecimal.valueOf(random.nextInt(100)));
            service.add(product);
        }
    }

    private List<String> readFileToList(String fileName) {
        InputStream in = WelcomeAction.class.getClassLoader().getResourceAsStream(fileName);
        Scanner sc = new Scanner(in);
        List<String> lines = new ArrayList<>();
        while (sc.hasNextLine())
            lines.add(sc.nextLine());
        return lines;
    }
}
