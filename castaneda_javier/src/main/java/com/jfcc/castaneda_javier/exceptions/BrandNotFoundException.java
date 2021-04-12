package com.jfcc.castaneda_javier.exceptions;

import com.jfcc.castaneda_javier.dto.CartDTO;
import lombok.Data;

@Data
public class BrandNotFoundException extends Exception{

    private CartDTO cart;

    public BrandNotFoundException(String message, CartDTO cart) {
        super(message);
        this.cart = cart;
    }

    public BrandNotFoundException(String message) {
        super(message);
    }
}
