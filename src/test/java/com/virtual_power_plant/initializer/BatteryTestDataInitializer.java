package com.virtual_power_plant.initializer;

import com.virtual_power_plant.model.dto.request.BatteryDTORequest;
import com.virtual_power_plant.model.dto.response.BatteryDTOResponse;
import com.virtual_power_plant.model.dto.response.BatteryStatsResponse;
import com.virtual_power_plant.model.entity.Battery;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BatteryTestDataInitializer {

    private Battery firstBattery;
    private Battery secondBattery;
    private BatteryDTORequest firstBatteryDTORequest;
    private BatteryDTORequest secondBatteryDTORequest;
    private BatteryStatsResponse batteryStatsResponse;
    private BatteryDTOResponse firstBatteryDTOResponse;
    private BatteryDTOResponse secondBatteryDTOResponse;

    public BatteryTestDataInitializer() {
        firstBattery = Battery.builder()
                .name("Tesla Powerwall 2")
                .wattCapacity(13500.0)
                .postCode(2000L)
                .isDeleted(false)
                .build();

        secondBattery = Battery.builder()
                .name("LG Chem RESU 10")
                .wattCapacity(9800.0)
                .postCode(2500L)
                .isDeleted(false)
                .build();

        firstBatteryDTORequest = BatteryDTORequest.builder()
                .name("Tesla Powerwall 2")
                .postCode(2000L)
                .wattCapacity(13500.0)
                .build();

        secondBatteryDTORequest = BatteryDTORequest.builder()
                .name("LG Chem RESU 10")
                .postCode(2500L)
                .wattCapacity(9800.0)
                .build();

        batteryStatsResponse = BatteryStatsResponse.builder()
                .batteryNames(List.of("Tesla Powerwall 2", "LG Chem RESU 10"))
                .averageWattCapacity(11650.0)
                .totalWattCapacity(23300.0).build();

        firstBatteryDTOResponse = BatteryDTOResponse.builder()
                .name("Tesla Powerwall 2")
                .postCode(2000L)
                .wattCapacity(13500.0)
                .build();

        secondBatteryDTOResponse = BatteryDTOResponse.builder()
                .name("LG Chem RESU 10")
                .postCode(2500L)
                .wattCapacity(9800.0)
                .build();

    }

}
