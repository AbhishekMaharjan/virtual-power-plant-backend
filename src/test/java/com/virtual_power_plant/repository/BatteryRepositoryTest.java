package com.virtual_power_plant.repository;

import com.virtual_power_plant.initializer.BatteryTestDataInitializer;
import com.virtual_power_plant.model.entity.Battery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BatteryRepositoryTest {

    @Autowired
    private BatteryRepository batteryRepository;

    private BatteryTestDataInitializer batteryTestDataInitializer;

    private static Stream<Arguments> postcodeRangeProvider() {
        return Stream.of(
                Arguments.of(1000L, 2000L, 5000.0, 15000.0),
                Arguments.of(1500L, 2500L, null, 15000.0),
                Arguments.of(2000L, 3000L, 5000.0, null)
        );
    }

    @BeforeEach
    public void setUp() {
        batteryTestDataInitializer = new BatteryTestDataInitializer();
    }

    @Test
    void givenBatteryId_whenFindById_ThenReturnsBattery() {
        var newBattery = batteryTestDataInitializer.getFirstBattery();
        var savedBatteryId = batteryRepository.save(newBattery).getId();

        var fetchedBattery = batteryRepository.findById(savedBatteryId);

        assertThat(fetchedBattery).isPresent();
        assertThat(fetchedBattery.orElseThrow()).extracting(
                Battery::getName,
                Battery::getCapacity
        ).containsExactly(
                newBattery.getName(),
                newBattery.getCapacity()
        );
    }

    @Test
    void givenNewBattery_whenSaved_ThenReturnsSavedBattery() {
        var newBattery = batteryTestDataInitializer.getFirstBattery();

        var savedBattery = batteryRepository.save(newBattery);

        assertThat(savedBattery).extracting(
                Battery::getId,
                Battery::getName
        ).containsExactly(
                newBattery.getId(),
                newBattery.getName()
        );

        assertNotNull(savedBattery.getCreatedDate());
        assertNotNull(savedBattery.getModifiedDate());
    }

    @ParameterizedTest
    @MethodSource("postcodeRangeProvider")
    void givenPostcodeRange_whenFindByPostcodeBetweenRange_ThenReturnsBatteriesSortedByNameASC(Long startpostcode,
                                                                                               Long endpostcode,
                                                                                               Double minCapacity,
                                                                                               Double maxCapacity) {
        var batteries = List.of(batteryTestDataInitializer.getFirstBattery(), batteryTestDataInitializer.getSecondBattery());
        batteryRepository.saveAll(batteries);

        var fetchedBatteryList = batteryRepository.findBatteriesByPostcodeOrCapacity(startpostcode, endpostcode,
                minCapacity, maxCapacity);

        assertThat(fetchedBatteryList).isNotEmpty();
        assertThat(fetchedBatteryList)
                .extracting(Battery::getPostcode)
                .allMatch(postcode -> postcode >= startpostcode && postcode <= endpostcode);
        assertThat(fetchedBatteryList)
                .extracting(Battery::getCapacity)
                .allMatch(capacity -> {
                    boolean isWithinMin = Objects.isNull(minCapacity) || capacity >= minCapacity;
                    boolean isWithinMax = Objects.isNull(maxCapacity) || capacity <= maxCapacity;
                    return isWithinMin && isWithinMax;
                });
    }

}