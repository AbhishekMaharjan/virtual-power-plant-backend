package com.virtual_power_plant.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatteryDTORequest {
    @NotNull
    private String name;
    private Long postCode;
    @NotNull
    private Double wattCapacity;
    private Boolean isDeleted;
}
