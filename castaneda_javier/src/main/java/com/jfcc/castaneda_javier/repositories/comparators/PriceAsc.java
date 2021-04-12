package com.jfcc.castaneda_javier.repositories.comparators;

import com.jfcc.castaneda_javier.dto.ProductDTO;

import java.util.Comparator;

public class PriceAsc implements Comparator<ProductDTO> {

    @Override
    public int compare(ProductDTO o1, ProductDTO o2) {
        return Double.compare(o1.getPrice(),o2.getPrice());
    }
}
