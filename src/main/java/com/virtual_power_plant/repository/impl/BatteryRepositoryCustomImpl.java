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

    public static final String POST_CODE = "postCode";
    public static final String WATT_CAPACITY = "wattCapacity";
    public static final String NAME = "name";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Battery> findBatteriesByPostCodeOrWattCapacity(@NonNull Long startpostCode,
                                                               @NonNull Long endPostCode,
                                                               Double minWattCapacity,
                                                               Double maxWattCapacity) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Battery> query = criteriaBuilder.createQuery(Battery.class);
        Root<Battery> batteryEntity = query.from(Battery.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.between(batteryEntity.get(POST_CODE), startpostCode, endPostCode));

        if (Objects.nonNull(minWattCapacity)) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(batteryEntity.get(WATT_CAPACITY), minWattCapacity));
        }
        if (Objects.nonNull(maxWattCapacity)) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(batteryEntity.get(WATT_CAPACITY), maxWattCapacity));
        }

        query.select(batteryEntity).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        query.orderBy(criteriaBuilder.asc(batteryEntity.get(NAME)));
        TypedQuery<Battery> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }
}
