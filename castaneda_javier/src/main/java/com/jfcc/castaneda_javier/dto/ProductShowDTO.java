package com.jfcc.castaneda_javier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductShowDTO {

    private int productId;
    private String product;
    private String category;
    private String brand;
    private String price;
    private int quantity;
    private String freeShipping;
    private String prestige;

}
