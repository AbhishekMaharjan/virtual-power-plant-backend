package com.virtual_power_plant.service;

import com.virtual_power_plant.exception.NotFoundException;
import com.virtual_power_plant.initializer.BatteryTestDataInitializer;
import com.virtual_power_plant.model.dto.response.BatteryDTOResponse;
import com.virtual_power_plant.model.dto.response.BatteryStatsResponse;
import com.virtual_power_plant.model.entity.Battery;
import com.virtual_power_plant.model.mapper.BatteryMapper;
import com.virtual_power_plant.repository.BatteryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BatteryServiceTest {

    public static final UUID batteryId = UUID.randomUUID();

    @Mock
    private BatteryRepository batteryRepository;

    @Mock
    private CustomMessageSource customMessageSource;

    @Mock
    private BatteryMapper batteryMapper;

    @InjectMocks
    private BatteryService batteryService;

    private BatteryTestDataInitializer batteryTestDataInitializer;

    @BeforeEach
    void setUp() {
        batteryTestDataInitializer = new BatteryTestDataInitializer();
    }

    @AfterEach
    void tearDown() {
        reset(batteryRepository, customMessageSource, batteryMapper);
    }

    @Test
    void givenExistedBattery_whenFindById_thenReturnsBattery() {
        var battery = batteryTestDataInitializer.getFirstBattery();

        when(batteryRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(battery));

        var fetchedBattery = batteryService.findById(batteryId);

        assertThat(fetchedBattery).isNotNull();
        assertThat(fetchedBattery).extracting(
                Battery::getId,
                Battery::getName
        ).containsExactly(
                battery.getId(),
                battery.getName()
        );

        verify(batteryRepository, times(1)).findById(batteryId);
    }

    @Test
    void givenNonExistedBattery_whenFindById_thenThrowsNotFoundException() {
        when(batteryRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.empty());
        when(customMessageSource.get("battery")).thenReturn("Battery");
        when(customMessageSource.get("error.id.not.found", customMessageSource.get("battery")))
                .thenThrow(new NotFoundException("Battery id not found"));

        assertThatThrownBy(() -> batteryService.findById(batteryId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Battery id not found");

        verify(batteryRepository, times(1)).findById(batteryId);
    }

    @Test
    void givenListOfBatteryDTOs_whenSaved_thenReturnsSavedBatteryDTOs() {
        var batteryDTORequestList = List.of(
                batteryTestDataInitializer.getFirstBatteryDTORequest(),
                batteryTestDataInitializer.getSecondBatteryDTORequest()
        );

        var firstBattery = batteryTestDataInitializer.getFirstBattery();
        var secondBattery = batteryTestDataInitializer.getSecondBattery();
        var mockBatteries = List.of(firstBattery, secondBattery);

        var firstBatteryDTOResponse = batteryTestDataInitializer.getFirstBatteryDTOResponse();
        var secondBatteryDTOResponse = batteryTestDataInitializer.getSecondBatteryDTOResponse();

        when(batteryMapper.toEntity(batteryTestDataInitializer.getFirstBatteryDTORequest())).thenReturn(firstBattery);
        when(batteryMapper.toEntity(batteryTestDataInitializer.getSecondBatteryDTORequest())).thenReturn(secondBattery);

        when(batteryRepository.saveAll(Mockito.anyList())).thenReturn(mockBatteries);

        when(batteryMapper.toDTOResponse(firstBattery)).thenReturn(firstBatteryDTOResponse);
        when(batteryMapper.toDTOResponse(secondBattery)).thenReturn(secondBatteryDTOResponse);

        List<BatteryDTOResponse> savedBatteryDTOResponses = batteryService.save(batteryDTORequestList);

        assertThat(savedBatteryDTOResponses).isNotNull().hasSize(2);
        assertThat(savedBatteryDTOResponses).extracting(
                BatteryDTOResponse::getName,
                BatteryDTOResponse::getWattCapacity
        ).containsExactlyInAnyOrder(
                tuple(firstBatteryDTOResponse.getName(), firstBatteryDTOResponse.getWattCapacity()),
                tuple(secondBatteryDTOResponse.getName(), secondBatteryDTOResponse.getWattCapacity())
        );

        verify(batteryRepository, times(1)).saveAll(Mockito.anyList());
        verify(batteryMapper, times(2)).toEntity(Mockito.any());
        verify(batteryMapper, times(2)).toDTOResponse(Mockito.any());
    }


    @Test
    void givenExistedBattery_whenUpdated_thenReturnsModifyBattery() {
        var existingBattery = batteryTestDataInitializer.getFirstBattery();
        var batteryDTORequest = batteryTestDataInitializer.getSecondBatteryDTORequest();
        var secondBatteryDTOResponse = batteryTestDataInitializer.getSecondBatteryDTOResponse();

        when(batteryRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(existingBattery));
        when(batteryRepository.save(Mockito.any(Battery.class))).thenReturn(existingBattery);
        when(batteryMapper.toDTOResponse(existingBattery)).thenReturn(secondBatteryDTOResponse);

        var updatedBattery = batteryService.update(batteryDTORequest, batteryId);

        assertThat(updatedBattery).isNotNull();
        assertThat(updatedBattery).extracting(
                BatteryDTOResponse::getName,
                BatteryDTOResponse::getWattCapacity
        ).containsExactly(
                secondBatteryDTOResponse.getName(),
                secondBatteryDTOResponse.getWattCapacity()
        );

        verify(batteryRepository, times(1)).findById(batteryId);
        verify(batteryRepository, times(1)).save(Mockito.any(Battery.class));
    }

    @Test
    void givenExistedBattery_whenDeleted_thenMarksAsDeleted() {
        Battery existingBattery = batteryTestDataInitializer.getFirstBattery();

        when(batteryRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.of(existingBattery));
        when(batteryRepository.save(Mockito.any(Battery.class))).thenReturn(existingBattery);

        Boolean result = batteryService.delete(batteryId);

        assertThat(result).isTrue();
        assertThat(batteryRepository.existsById(batteryId)).isFalse();

        verify(batteryRepository, times(1)).findById(batteryId);
        verify(batteryRepository, times(1)).save(existingBattery);
    }

    @Test
    void givenBatteriesInRange_whenFetchedByPostCodeBetweenGivenRange_thenReturnBatteryStatisticsResponse() {
        List<Battery> batteries = List.of(batteryTestDataInitializer.getFirstBattery(), batteryTestDataInitializer.getSecondBattery());

        when(batteryRepository.findBatteriesByPostCodeOrWattCapacity(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(batteries);

        BatteryStatsResponse batteryStatsResponse = batteryService.getBatteries(2000L, 10000L,
                45.5, 85.5);

        assertThat(batteryStatsResponse).isNotNull();
        assertThat(batteryStatsResponse).extracting(
                BatteryStatsResponse::getBatteryNames,
                BatteryStatsResponse::getTotalWattCapacity,
                BatteryStatsResponse::getAverageWattCapacity
        ).containsExactly(
                batteries.stream().map(Battery::getName).toList(),
                23300.0,
                11650.0
        );

        verify(batteryRepository, times(1)).findBatteriesByPostCodeOrWattCapacity(
                Mockito.anyLong(),
                Mockito.anyLong(),
                Mockito.anyDouble(),
                Mockito.anyDouble());
    }

}
