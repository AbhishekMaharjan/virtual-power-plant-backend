package com.virtual_power_plant.controller;

import com.virtual_power_plant.container.PostgreSQLBaseTestContainer;
import com.virtual_power_plant.initializer.BatteryTestDataInitializer;
import com.virtual_power_plant.model.dto.request.BatteryDTORequest;
import com.virtual_power_plant.model.dto.response.BatteryDTOResponse;
import com.virtual_power_plant.model.dto.response.BatteryStatsResponse;
import com.virtual_power_plant.model.dto.response.GlobalApiResponse;
import com.virtual_power_plant.repository.BatteryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BatteryControllerIntegrationTest extends PostgreSQLBaseTestContainer {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BatteryRepository batteryRepository;

    private BatteryTestDataInitializer batteryTestDataInitializer;

    @BeforeEach
    void setUp() {
        batteryTestDataInitializer = new BatteryTestDataInitializer();
    }

    @AfterEach
    void reset() {
        batteryRepository.deleteAll();
    }

    @Test
    void givenValidBatterySearchParams_whenSearched_thenReturnsBatteryListWithOkStatus() {
        var savedBattery = batteryRepository.save(batteryTestDataInitializer.getFirstBattery());

        ResponseEntity<GlobalApiResponse<BatteryStatsResponse>> response = testRestTemplate.exchange(
                "/api/v1/battery?startPostCode=1000&endPostCode=2000",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                });

        assertThat(response)
                .extracting(
                        ResponseEntity::getStatusCode,
                        fetchedBatteryNames -> Objects.requireNonNull(fetchedBatteryNames.getBody()).getData().getBatteryNames()
                )
                .containsExactly(
                        HttpStatus.OK,
                        List.of(savedBattery.getName())
                );
    }

    @Test
    void givenListOfBatteryDTORequest_whenSaved_thenReturnsCreatedStatus() {
        var batteryList = List.of(
                batteryTestDataInitializer.getFirstBatteryDTORequest(),
                batteryTestDataInitializer.getSecondBatteryDTORequest()
        );

        HttpEntity<List<BatteryDTORequest>> request = new HttpEntity<>(batteryList);

        ResponseEntity<GlobalApiResponse<List<BatteryDTOResponse>>> response = testRestTemplate.exchange(
                "/api/v1/battery",
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                });

        assertThat(response)
                .extracting(
                        ResponseEntity::getStatusCode,
                        savedFirstBattery -> Objects.requireNonNull(savedFirstBattery.getBody()).getData().get(0).getName(),
                        savedSecondBattery -> Objects.requireNonNull(savedSecondBattery.getBody()).getData().get(1).getName()
                )
                .containsExactly(
                        HttpStatus.CREATED,
                        batteryTestDataInitializer.getFirstBatteryDTORequest().getName(),
                        batteryTestDataInitializer.getSecondBatteryDTORequest().getName()
                );
    }

    @Test
    void givenValidBatteryDetails_whenUpdated_thenReturnsUpdatedBatteryWithOkStatus() {
        var savedBattery = batteryRepository.save(batteryTestDataInitializer.getFirstBattery());
        var batteryId = savedBattery.getId();
        var batteryDTORequest = batteryTestDataInitializer.getFirstBatteryDTORequest();

        HttpEntity<BatteryDTORequest> request = new HttpEntity<>(batteryDTORequest);

        ResponseEntity<GlobalApiResponse<BatteryDTOResponse>> response = testRestTemplate.exchange(
                "/api/v1/battery/" + batteryId,
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<>() {
                });

        assertThat(response)
                .extracting(
                        ResponseEntity::getStatusCode,
                        updatedBattery -> Objects.requireNonNull(updatedBattery.getBody()).getData().getName(),
                        updatedBattery -> Objects.requireNonNull(updatedBattery.getBody()).getData().getWattCapacity()
                )
                .containsExactly(
                        HttpStatus.OK,
                        batteryDTORequest.getName(),
                        batteryDTORequest.getWattCapacity()
                );
    }

    @Test
    void givenExistingBatteryId_whenDeleted_thenReturnsOkStatus() {
        var savedBattery = batteryRepository.save(batteryTestDataInitializer.getFirstBattery());
        var batteryId = savedBattery.getId();
        ResponseEntity<GlobalApiResponse<Object>> response = testRestTemplate.exchange(
                "/api/v1/battery/" + batteryId,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                });

        assertThat(response).extracting(ResponseEntity::getStatusCode).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(batteryRepository.existsById(batteryId)).isFalse();
    }
}
