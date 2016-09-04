package com.msfrolov.pricelist.extra;

import com.msfrolov.pricelist.action.WelcomeAction;
import com.msfrolov.pricelist.model.Category;
import com.msfrolov.pricelist.model.Product;
import com.msfrolov.pricelist.service.PriceService;
import com.msfrolov.pricelist.service.PriceServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class InitContent {

    private static final Logger log = LoggerFactory.getLogger(InitContent.class);

    public void initContent() {
        //given
        PriceService service = new PriceServiceImpl();
        List<Product> all = service.findAll(Product.class);
        //when
        if (all.isEmpty()) {
            List<String> proView = readFileToList("price/1Monitor ProView.txt");
            List<String> acer = readFileToList("price/2Monitor Sony.txt");
            List<String> sony = readFileToList("price/3Notebook Acer.txt");
            //then
            handleList("Monitor ProView series: ABBFHYVO", proView, service);
            handleList("Notebook Acer series: BGFYEADCB", acer, service);
            handleList("Notebook Acer series: AVCHYDBCN", sony, service);
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
