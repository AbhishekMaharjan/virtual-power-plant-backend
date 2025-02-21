package com.virtual_power_plant.repository;

import com.virtual_power_plant.model.entity.Battery;

import java.util.List;

public interface BatteryRepositoryCustom {

    List<Battery> findBatteriesByPostcodeOrCapacity(Long startPostcode,
                                                    Long endPostcode,
                                                    Double minCapacity,
                                                    Double maxCapacity);
}
