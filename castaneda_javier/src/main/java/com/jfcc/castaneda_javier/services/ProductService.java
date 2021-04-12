package com.jfcc.castaneda_javier.services;

import com.jfcc.castaneda_javier.dto.CartDTO;
import com.jfcc.castaneda_javier.dto.ProductShowDTO;
import com.jfcc.castaneda_javier.dto.ResponsePurchaseDTO;
import com.jfcc.castaneda_javier.exceptions.*;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    public List<ProductShowDTO> getAllProducts() throws IOException;
    public List<ProductShowDTO> getFiltered(Integer productId, String name, String category, String brand, Boolean freeShipping, Integer prestige, Double price, Integer order) throws IOException, ManyParamsException, NotValidOrderException;
    public ResponsePurchaseDTO makePurchase(CartDTO cart) throws NotEnoughQuantityException, NameNotFoundException, IOException, ProductIdNotValidException, BrandNotFoundException;
}
