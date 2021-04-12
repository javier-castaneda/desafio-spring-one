package com.jfcc.castaneda_javier.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class TicketFailDTO extends TicketDTO{

    public TicketFailDTO(List<ProductPurchaseDTO> list){
        super(list);
    }

}
