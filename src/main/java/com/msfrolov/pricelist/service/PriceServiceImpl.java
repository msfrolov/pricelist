package com.msfrolov.pricelist.service;

import com.msfrolov.pricelist.exception.PriceException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

public class PriceServiceImpl implements PriceService {


    //    @Override
    //    public List<Product> findAll() {
    //        ArrayList<Product> products = new ArrayList<>();
    //        Category category = new Category();
    //        category.setId(11);
    //        category.setName("Category");
    //        for (int i = 1; i <= 10; i++) {
    //            Product product = new Product();
    //            product.setId(i);
    //            product.setName("Name");
    //            product.setCategory(category);
    //            product.setPrice(Money.parse("KZT 20"));
    //            products.add(product);
    //        }
    //        return products;
    //    }


    private EntityManager entityManager;

    public PriceServiceImpl() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("pricelist");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    @Override public <T> T add(T product) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(product);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new PriceException("failed to add entity in db", e);
        }
        entityManager.getTransaction().commit();
        return product;
    }


    @Override public <T> List<T> findAll(Class clazz) {
        TypedQuery<T> namedQuery = entityManager.createNamedQuery("Product.getAll", clazz);
        return namedQuery.getResultList();
    }

    @Override public  <T> List<T> findByFilters(Map<String, Object> filters, Class clazz) {
        return null;
    }
}
