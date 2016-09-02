package com.msfrolov.pricelist.service;

import java.util.List;
import java.util.Map;

public interface PriceService {

    <T> T add(T product);

    <T> List<T> findAll(Class clazz);

    <T> List<T> findByFilters(Map<String, Object> filters, Class clazz);

}




