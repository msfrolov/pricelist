package com.msfrolov.pricelist.service;

import com.msfrolov.pricelist.exception.PriceException;
import com.msfrolov.pricelist.model.Category;
import com.msfrolov.pricelist.model.Product;

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

    @Override public Product add(Product product) {
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

    @Override public Category addCat(Category category) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(category);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new PriceException("failed to add entity in db", e);
        }
        entityManager.getTransaction().commit();
        return category;
    }

    @Override public List<Product> findAll() {
        TypedQuery<Product> namedQuery = entityManager.createNamedQuery("Product.getAll", Product.class);
        return namedQuery.getResultList();
    }

    @Override public List<Product> findByFilters(Map<String, Object> criteria) {
        return null;
    }
}
