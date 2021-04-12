package com.jfcc.castaneda_javier.repositories.comparators;

import com.jfcc.castaneda_javier.dto.ProductDTO;

import java.util.Comparator;

public class NameDesc implements Comparator<ProductDTO> {
    @Override
    public int compare(ProductDTO o1, ProductDTO o2) {
        return o2.getProduct().compareTo(o1.getProduct());
    }
}
