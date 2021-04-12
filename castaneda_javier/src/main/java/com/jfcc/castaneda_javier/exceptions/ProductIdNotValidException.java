package com.jfcc.castaneda_javier.exceptions;

import com.jfcc.castaneda_javier.dto.CartDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductIdNotValidException extends Exception{

    private CartDTO cart;

    public ProductIdNotValidException(String message){
        super(message);
    }

    public ProductIdNotValidException(String message, CartDTO cart){
        super(message);
        this.cart = cart;
    }
}
