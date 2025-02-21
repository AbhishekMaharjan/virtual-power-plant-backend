package com.virtual_power_plant.repository.impl;

import com.virtual_power_plant.model.entity.Battery;
import com.virtual_power_plant.repository.BatteryRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class BatteryRepositoryCustomImpl implements BatteryRepositoryCustom {

    public static final String postcode = "postcode";
    public static final String CAPACITY = "capacity";
    public static final String NAME = "name";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Battery> findBatteriesByPostcodeOrCapacity(@NonNull Long startPostcode,
                                                           @NonNull Long endPostcode,
                                                           Double minCapacity,
                                                           Double maxCapacity) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Battery> query = criteriaBuilder.createQuery(Battery.class);
        Root<Battery> batteryEntity = query.from(Battery.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.between(batteryEntity.get(postcode), startPostcode, endPostcode));

        if (Objects.nonNull(minCapacity)) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(batteryEntity.get(CAPACITY), minCapacity));
        }
        if (Objects.nonNull(maxCapacity)) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(batteryEntity.get(CAPACITY), maxCapacity));
        }

        query.select(batteryEntity).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        query.orderBy(criteriaBuilder.asc(batteryEntity.get(NAME)));
        TypedQuery<Battery> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
