package com.msfrolov.pricelist.service;

import com.msfrolov.pricelist.exception.PriceException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

public class PriceServiceImpl implements PriceService {

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

    @Override public <T> List<T> findByFilters(Map<String, Object> filters, Class clazz) {
        if (filters.isEmpty()){
            return null;
        }
//        create query
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);
        Root<T> root = query.from(clazz);
//        check name
        String name = (String) filters.get("name");
        if (name!=null) {
            Expression<String> path = root.get("name");
            Predicate like = criteriaBuilder.like(path, name + "%");
            query.where(like);
        }
        query.select(root);

//        query.
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
