package com.virtual_power_plant.model.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BatteryStatsResponse {
    private List<String> batteryNames;
    private Double totalCapacity;
    private Double averageCapacity;

}
