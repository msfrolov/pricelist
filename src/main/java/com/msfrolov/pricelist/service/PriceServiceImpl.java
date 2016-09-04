package com.msfrolov.pricelist.service;

import com.msfrolov.pricelist.exception.PriceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PriceServiceImpl implements PriceService {

    private static final Logger log = LoggerFactory.getLogger(PriceServiceImpl.class);
    private EntityManager entityManager;

    public PriceServiceImpl() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("com.msfrolov.pricelist");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * @param entity Entity for storage in the db
     * @param <T>    Entity generic type
     * @return added entity with ID
     */
    @Override public <T> T add(T entity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new PriceException("failed to add entity in db", e);
        }
        entityManager.getTransaction().commit();
        return entity;
    }

    /**
     * @param clazz Entity class
     * @param <T>   Entity generic type
     * @return all entity from DB
     */
    @Override public <T> List<T> findAll(Class clazz) {
        TypedQuery<T> namedQuery = entityManager.createNamedQuery("Product.getAll", clazz);
        return namedQuery.getResultList();
    }

    /**
     * @param filters Map with filter values from UI
     * @param clazz   Entity class
     * @param <T>     Entity generic type
     * @return result list with entity which match criteria
     */
    @Override public <T> List<T> findByFilters(Map<String, Object> filters, Class clazz) {
        log.debug("Filters: cont-{}", filters);
        if (filters.isEmpty()) {
            return null;
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> entity = criteriaQuery.from(clazz);
        Predicate[] predicates = applyFilters(filters, criteriaBuilder, criteriaQuery, entity);
        criteriaQuery.where(predicates);
        criteriaQuery.select(entity);
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        List<T> resultList = typedQuery.getResultList();
        log.debug("Result: {}", resultList);
        return resultList;
    }

    /**
     * Using predicates in Criteria API to filter selection in the query
     */
    private <T> Predicate[] applyFilters(Map<String, Object> filters, CriteriaBuilder criteriaBuilder,
            CriteriaQuery<T> criteriaQuery, Root<T> entity) {
        List<Predicate> predicates = new ArrayList<>();
        //         apply name filter
        String name = (String) filters.get("name");
        log.debug("Filter value name: {}", name);
        if (name != null) {
            Path<String> path = entity.get("name");
            Predicate likeName = criteriaBuilder.like(criteriaBuilder.lower(path), (name + "%").toLowerCase());
            predicates.add(likeName);
        }
        //         apply category filter
        String category = (String) filters.get("category");
        log.debug("Filter value category: {}", category);
        if (category != null) {
            //            Root<Category> categoryRoot = criteriaQuery.from(Category.class);
            Path<String> path = entity.get("category").get("name");
            Predicate likeCategory = criteriaBuilder.like(criteriaBuilder.lower(path), (category + "%").toLowerCase());
            predicates.add(likeCategory);
        }
        //         apply price from filter
        BigDecimal priceFrom = (BigDecimal) filters.get("price_from");
        log.debug("Filter value price_from: {}", priceFrom);
        if (priceFrom != null) {
            Path<BigDecimal> path = entity.get("price");
            Predicate gtPrice = criteriaBuilder.greaterThanOrEqualTo(path, priceFrom);
            predicates.add(gtPrice);
        }
        //         apply price to filter
        BigDecimal priceTo = (BigDecimal) filters.get("price_to");
        log.debug("Filter value price_to: {}", priceTo);
        if (priceTo != null) {
            Path<BigDecimal> path = entity.get("price");
            Predicate lsPrice = criteriaBuilder.lessThanOrEqualTo(path, priceTo);
            predicates.add(lsPrice);
        }
        return predicates.toArray(new Predicate[predicates.size()]);
    }
}
