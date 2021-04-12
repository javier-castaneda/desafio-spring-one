package com.jfcc.castaneda_javier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class TicketOkDTO extends TicketDTO{
    private int id;
    private long total;

    public TicketOkDTO(CartDTO cart, int id, long total){
        super(cart.getArticles());
        this.id = id;
        this.total = total;
    }
}
