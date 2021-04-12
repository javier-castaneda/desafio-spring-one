package com.jfcc.castaneda_javier.exceptions;

import com.jfcc.castaneda_javier.dto.CartDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotEnoughQuantityException extends Exception{

    private CartDTO cart;
    private int quantity;

    public NotEnoughQuantityException(String message, int quantity){
        super(message);
        this.quantity = quantity;
    }

    public NotEnoughQuantityException(String message, int quantity, CartDTO cart){
        super(message);
        this.quantity =quantity;
        this.cart = cart;
    }

}
