package com.jfcc.castaneda_javier.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String email;
    private String userName;
    private String password;
    private CartDTO personalCart;

}
