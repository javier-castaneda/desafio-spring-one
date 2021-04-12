package com.jfcc.castaneda_javier.repositories;

import com.jfcc.castaneda_javier.dto.*;
import com.jfcc.castaneda_javier.exceptions.*;

import java.io.IOException;
import java.util.List;


public interface ProductRepository {

    List<ProductDTO> listAllProducts() throws IOException;
    List<ProductDTO> listFilteredProducts(Integer productId, String name, String category, String brand, Boolean freeShipping, Integer prestige, Double price, Integer order) throws IOException, ManyParamsException, NotValidOrderException;
    boolean productAvailable(ProductPurchaseDTO prod) throws IOException, ProductIdNotValidException, NameNotFoundException, BrandNotFoundException, NotEnoughQuantityException;
    StatusCodeDTO checkAviability(CartDTO cart) throws NameNotFoundException, BrandNotFoundException, ProductIdNotValidException, IOException, NotEnoughQuantityException;
    TicketOkDTO makePurchase(CartDTO cart) throws NotEnoughQuantityException, NameNotFoundException, BrandNotFoundException, ProductIdNotValidException, IOException;

}
