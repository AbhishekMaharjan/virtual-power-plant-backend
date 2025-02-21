package com.virtual_power_plant.model.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatteryDTOResponse {
    private UUID id;
    private String name;
    private Long postCode;
    private Double wattCapacity;
}
