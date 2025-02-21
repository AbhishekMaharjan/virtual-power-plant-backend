package com.virtual_power_plant.controller;

import com.virtual_power_plant.exception.NotFoundException;
import com.virtual_power_plant.model.dto.request.BatteryDTORequest;
import com.virtual_power_plant.model.dto.response.BatteryDTOResponse;
import com.virtual_power_plant.model.dto.response.BatteryStatsResponse;
import com.virtual_power_plant.model.dto.response.GlobalApiResponse;
import com.virtual_power_plant.service.BatteryService;
import com.virtual_power_plant.service.CustomMessageSource;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.virtual_power_plant.constant.FieldConstantValue.BATTERY;
import static com.virtual_power_plant.constant.SuccessResponseConstant.*;

@RestController
@RequestMapping(value = "${api.base-path}/battery")
public class BatteryController extends BaseController {

    @NonNull
    private final BatteryService batteryService;

    protected BatteryController(@NonNull CustomMessageSource customMessageSource,
                                @NonNull BatteryService batteryService) {
        super(customMessageSource);
        this.batteryService = batteryService;
    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse<BatteryStatsResponse>> search(
            @RequestParam(defaultValue = "1000") Long startPostCode,
            @RequestParam(defaultValue = "9999") Long endPostCode,
            @RequestParam(required = false) Double minWattCapacity,
            @RequestParam(required = false) Double maxWattCapacity) {

        return buildFetchedResponse(
                customMessageSource.getMessage(FETCHED_LIST, BATTERY),
                batteryService.getBatteries(startPostCode, endPostCode, minWattCapacity, maxWattCapacity)
        );
    }

    @PostMapping
    public ResponseEntity<GlobalApiResponse<List<BatteryDTOResponse>>> save(
            @Valid @RequestBody List<BatteryDTORequest> batteryDTORequestList) {

        return buildSavedResponse(
                customMessageSource.getMessage(SUCCESS_SAVE, BATTERY),
                batteryService.save(batteryDTORequestList)
        );
    }

    @PutMapping("/{batteryId}")
    public ResponseEntity<GlobalApiResponse<BatteryDTOResponse>> update(
            @RequestBody BatteryDTORequest batteryDTORequest,
            @PathVariable UUID batteryId) throws NotFoundException {

        return buildUpdatedResponse(
                customMessageSource.getMessage(SUCCESS_UPDATE, BATTERY),
                batteryService.update(batteryDTORequest, batteryId)
        );
    }

    @DeleteMapping("/{batteryId}")
    public ResponseEntity<GlobalApiResponse<Object>> delete(@PathVariable UUID batteryId) throws NotFoundException {

        batteryService.delete(batteryId);
        return buildDeletedResponse(customMessageSource.getMessage(SUCCESS_DELETE, BATTERY));
    }

}
