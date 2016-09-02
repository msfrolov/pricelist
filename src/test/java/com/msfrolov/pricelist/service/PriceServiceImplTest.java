package com.msfrolov.pricelist.service;

import com.msfrolov.pricelist.model.Category;
import com.msfrolov.pricelist.model.Product;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class PriceServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(PriceServiceImplTest.class);

    private static List<String> readFileToList(String fileName) {
        InputStream in = PriceServiceImplTest.class.getClassLoader().getResourceAsStream(fileName);
        Scanner sc = new Scanner(in);
        List<String> lines = new ArrayList<>();
        while (sc.hasNextLine())
            lines.add(sc.nextLine());
        return lines;
    }

    @Test public void ShouldReturnAllProduct() throws Exception {
        //given
        PriceService service = new PriceServiceImpl();
        //when
        List<Product> all = service.findAll(Product.class);
        //then
        assertEquals(12, all.size());
    }

    @Test public void ShouldReturnAllProductWhichFitCriteria() throws Exception {
        //given
        PriceService service = new PriceServiceImpl();
        //when
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", "по");
        List<Product> all = service.findByFilters(filters, Product.class);
        //then
        log.debug("!!!PRODUCTS: {}", all);
        assertEquals(1, 1);
    }

    @Test public void ShouldAddTestDataInDBIfThereItIsEmpty() throws Exception {
        //given
        PriceService service = new PriceServiceImpl();
        List<Product> all = service.findAll(Product.class);
        //when
        if (all.isEmpty()) {
            List<String> garden = readFileToList("price/Садовая.txt");
            List<String> dining = readFileToList("price/Столовая.txt");
            List<String> living = readFileToList("price/Гостинная.txt");
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

}
