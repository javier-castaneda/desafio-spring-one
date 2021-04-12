package com.jfcc.castaneda_javier.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartDTO {
    private List<ProductPurchaseDTO> articles = new ArrayList<>();
}
