package com.virtual_power_plant.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static com.virtual_power_plant.constant.FieldConstantValue.BATTERY;
import static com.virtual_power_plant.constant.SuccessResponseConstant.FETCHED_LIST;

@Component
public class CustomMessageSource {

    private final MessageSource messageSource;

    public CustomMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String get(String code) {
        return messageSource.getMessage(code, null, Locale.ENGLISH);
    }

    public String get(String code, Object... objects) {
        return messageSource.getMessage(code, objects, Locale.ENGLISH);
    }

    public String getMessage(String operationType, String data){
        return get(operationType, get(data));
    }

}
