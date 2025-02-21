package com.virtual_power_plant.repository;

import com.virtual_power_plant.model.entity.Battery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BatteryRepository extends JpaRepository<Battery, UUID>, BatteryRepositoryCustom {
}
