package com.virtual_power_plant.model.mapper;

import com.virtual_power_plant.model.dto.request.BatteryDTORequest;
import com.virtual_power_plant.model.dto.response.BatteryDTOResponse;
import com.virtual_power_plant.model.entity.Battery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BatteryMapper {

    BatteryDTOResponse toDTOResponse(Battery battery);

    Battery toEntity(BatteryDTORequest batteryDTORequest);
}
