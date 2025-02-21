package com.virtual_power_plant.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalApiResponse<T> {
    private Boolean status;
    private String message;
    private T data;
    private List<String> error;

    public GlobalApiResponse(String message, T data) {
        this.setStatus(true);
        this.setMessage(message);
        this.setData(data);
    }
}
