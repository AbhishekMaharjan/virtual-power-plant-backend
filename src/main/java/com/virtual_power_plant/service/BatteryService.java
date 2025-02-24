package com.virtual_power_plant.service;

import com.virtual_power_plant.exception.NotFoundException;
import com.virtual_power_plant.model.dto.request.BatteryDTORequest;
import com.virtual_power_plant.model.dto.response.BatteryDTOResponse;
import com.virtual_power_plant.model.dto.response.BatteryStatsResponse;
import com.virtual_power_plant.model.entity.Battery;
import com.virtual_power_plant.model.mapper.BatteryMapper;
import com.virtual_power_plant.repository.BatteryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.virtual_power_plant.constant.ErrorConstantValue.ERROR_ID_NOT_FOUND;
import static com.virtual_power_plant.constant.FieldConstantValue.BATTERY;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatteryService {

    @NonNull
    private final BatteryRepository batteryRepository;

    @NonNull
    private final CustomMessageSource customMessageSource;

    @NonNull
    private final BatteryMapper batteryMapper;

    public Battery findById(UUID id) {
        return batteryRepository.findById(id).orElseThrow(() ->
                new NotFoundException(customMessageSource.get(ERROR_ID_NOT_FOUND, customMessageSource.get(BATTERY))));
    }

    @Transactional
    public List<BatteryDTOResponse> save(List<BatteryDTORequest> batteryDTORequestList) {
        var batteries = batteryDTORequestList.stream().map(batteryMapper::toEntity).toList();
        var savedBatteries = batteryRepository.saveAll(batteries);

        return savedBatteries.stream().map(batteryMapper::toDTOResponse).toList();
    }

    public BatteryDTOResponse update(BatteryDTORequest batteryDTORequest, UUID batteryId) {
        var battery = findById(batteryId);
        battery.setName(batteryDTORequest.getName());
        battery.setPostcode(batteryDTORequest.getPostcode());
        battery.setCapacity(batteryDTORequest.getCapacity());
        battery = batteryRepository.save(battery);

        log.warn("Battery with UUID {} has been updated with capacity {}.", batteryId, battery.getCapacity());
        return batteryMapper.toDTOResponse(battery);
    }

    public Boolean delete(UUID batteryId) {
        var battery = findById(batteryId);
        battery.setIsDeleted(true);
        batteryRepository.save(battery);

        log.info("Battery with UUID {} has been deleted.", batteryId);
        return true;
    }

    public BatteryStatsResponse getBatteries(Long startPostcode, Long endPostcode, Double minCapacity, Double maxCapacity) {
        var batteryList = batteryRepository.findBatteriesByPostcodeOrCapacity(startPostcode, endPostcode, minCapacity, maxCapacity);

        if (batteryList.isEmpty()) {
            return new BatteryStatsResponse();
        }

        var names = batteryList.stream().map(Battery::getName).toList();
        var totalCapacity = batteryList.stream().mapToDouble(Battery::getCapacity).sum();
        var averageCapacity = totalCapacity / batteryList.size();
        var formatted = String.format("%.2f", averageCapacity);
        averageCapacity = Double.parseDouble(formatted);

        var batteryStatsResponse = BatteryStatsResponse.builder()
                .batteryNames(names)
                .totalCapacity(totalCapacity)
                .averageCapacity(averageCapacity).build();

        log.info("No of batteries retrieved : {}", batteryList.size());
        return batteryStatsResponse;
    }

}
