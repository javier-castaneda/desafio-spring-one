package com.jfcc.castaneda_javier.services;

import com.jfcc.castaneda_javier.dto.*;
import com.jfcc.castaneda_javier.exceptions.*;
import com.jfcc.castaneda_javier.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{


    ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    //Permite obtener todos los productos que hay en la base de datos
    public List<ProductShowDTO> getAllProducts() throws IOException {

        List<ProductDTO> prods= productRepository.listAllProducts();
        List<ProductShowDTO> prodShows = new ArrayList<>();

        for (ProductDTO prod:prods) {
            prodShows.add(toView(prod));
        }

        return prodShows;
    }

    //Permite obtener los elementos de la base de datos filtrados
    @Override
    public List<ProductShowDTO> getFiltered(Integer productId, String name, String category, String brand, Boolean freeShipping, Integer prestige, Double price, Integer order) throws IOException, ManyParamsException, NotValidOrderException {
        List<ProductDTO> prods= productRepository.listFilteredProducts(productId,name,category,brand,freeShipping,prestige,price, order);
        List<ProductShowDTO> prodShows = new ArrayList<>();

        for (ProductDTO prod:prods) {
            prodShows.add(toView(prod));
        }

        return prodShows;

    }

    //Dado un carrito de compra, retorna una respuesta completa con el ticket y un StatusCode
    @Override
    public ResponsePurchaseDTO makePurchase(CartDTO cart) throws NotEnoughQuantityException, NameNotFoundException, IOException, ProductIdNotValidException, BrandNotFoundException {
        StatusCodeDTO status = productRepository.checkAviability(cart);
        ResponsePurchaseDTO response = new ResponsePurchaseDTO();
        response.setTicket(productRepository.makePurchase(cart));
        response.setStatusCode(status);
        return response;
    }

    //Formatea los elementos de la base de datos de para listarlos al usuario
    private ProductShowDTO toView(ProductDTO productDTO){
        ProductShowDTO productShowDTO = new ProductShowDTO();
        productShowDTO.setProductId(productDTO.getProductId());
        productShowDTO.setProduct(productDTO.getProduct());
        productShowDTO.setCategory(productDTO.getCategory());
        productShowDTO.setBrand(productDTO.getBrand());
        productShowDTO.setQuantity(productDTO.getQuantity());
        productShowDTO.setPrice("$"+String.valueOf(productDTO.getPrice()).replace(".0",""));
        if (productDTO.isFreeShipping())
            productShowDTO.setFreeShipping("SI");
        else
            productShowDTO.setFreeShipping("NO");
        String prestige ="";
        for (int i = 0; i < productDTO.getPrestige(); i++) {
            prestige+="*";
        }
        productShowDTO.setPrestige(prestige);
        return productShowDTO;
    }

}
