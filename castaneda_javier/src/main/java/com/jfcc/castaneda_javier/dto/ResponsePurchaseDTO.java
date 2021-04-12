package com.jfcc.castaneda_javier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePurchaseDTO {
    private TicketDTO ticket;
    private StatusCodeDTO statusCode;

}
