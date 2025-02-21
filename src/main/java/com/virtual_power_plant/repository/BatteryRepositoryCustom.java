package com.virtual_power_plant.repository;

import com.virtual_power_plant.model.entity.Battery;

import java.util.List;

public interface BatteryRepositoryCustom {

    List<Battery> findBatteriesByPostCodeOrWattCapacity(Long startPostCode,
                                                        Long endPostCode,
                                                        Double minWattCapacity,
                                                        Double maxWattCapacity);
}
