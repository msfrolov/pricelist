package com.msfrolov.pricelist.service;

import com.msfrolov.pricelist.model.Category;
import com.msfrolov.pricelist.model.Product;

import java.util.List;
import java.util.Map;

public interface PriceService {

    Product add(Product product);

    Category addCat(Category category);

    List<Product> findAll();

    List<Product> findByFilters(Map<String, Object> criteria);

}




