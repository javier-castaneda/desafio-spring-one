package com.jfcc.castaneda_javier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class TicketDTO {
    private List<ProductPurchaseDTO> articles;

    public TicketDTO(List<ProductPurchaseDTO> articles) {
        this.articles = articles;
    }

}
