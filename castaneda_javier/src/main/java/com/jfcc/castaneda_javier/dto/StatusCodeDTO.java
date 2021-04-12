package com.jfcc.castaneda_javier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusCodeDTO {
    private int statusCode;
    private String message;
}
