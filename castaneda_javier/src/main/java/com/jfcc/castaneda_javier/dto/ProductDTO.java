package com.jfcc.castaneda_javier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private int productId;
    private String product;
    private String category;
    private String brand;
    private long price;
    private int quantity;
    private boolean freeShipping;
    private int prestige;

}
