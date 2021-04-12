package com.jfcc.castaneda_javier.exceptions;


import com.jfcc.castaneda_javier.dto.CartDTO;
import lombok.Data;

@Data
public class NameNotFoundException extends Exception{

    private CartDTO cart;

    public NameNotFoundException(String message) {
        super(message);
    }

    public NameNotFoundException(String message, CartDTO cart) {
        super(message);
        this.cart = cart;
    }

}
