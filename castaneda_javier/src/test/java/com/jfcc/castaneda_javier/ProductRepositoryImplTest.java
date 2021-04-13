package com.jfcc.castaneda_javier;

import com.jfcc.castaneda_javier.dto.ProductDTO;
import com.jfcc.castaneda_javier.exceptions.ManyParamsException;
import com.jfcc.castaneda_javier.exceptions.NotValidOrderException;
import com.jfcc.castaneda_javier.repositories.ProductRepository;
import com.jfcc.castaneda_javier.repositories.ProductRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.jfcc.castaneda_javier.productlists.ProductListTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ProductRepositoryImplTest {

    @InjectMocks
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp(){
        productRepository = new ProductRepositoryImpl();
    }

    @Test
    public void shouldReturnAll() throws IOException {
        List<ProductDTO> result = productRepository.listAllProducts();
        assertThat(result).isEqualTo(productsFromFile());
    }

    @Test
    public void shouldReturnTools() throws NotValidOrderException, ManyParamsException, IOException {
        List<ProductDTO> filtered = productsFromFile().stream().filter(productDTO -> productDTO.getCategory()
                .equals("Herramienta")).collect(Collectors.toList());

        assertThat(filtered).isEqualTo(productRepository.listFilteredProducts(null, null,
                "Herramienta",null,null,null,null,null));
    }

    @Test
    public void shouldReturnToolsFreeShipping() throws NotValidOrderException, ManyParamsException, IOException {
        List<ProductDTO> filtered = productsFromFile().stream().filter(productDTO -> productDTO.getCategory()
                .equals("Herramienta")).filter(productDTO -> productDTO.isFreeShipping()).collect(Collectors.toList());

        assertThat(filtered).isEqualTo(productRepository.listFilteredProducts(null, null,
                "Herramienta",null,true,null,null,null));
    }

    @Test
    public void shouldReturnOrderedByName() throws NotValidOrderException, ManyParamsException, IOException {
        List<ProductDTO> ordered = productSFromFileOrderAlf();

        assertThat(ordered).isEqualTo(productRepository.listFilteredProducts(null,null,
                null,null,null,null,null,0));
    }

    @Test
    public void shouldReturnOrderedByNameInverse() throws NotValidOrderException, ManyParamsException, IOException {
        List<ProductDTO> ordered = productSFromFileOrderAlfInverse();

        assertThat(ordered).isEqualTo(productRepository.listFilteredProducts(null,null,
                null,null,null,null,null,1));
    }

    @Test
    public void shouldReturnOrderedByPrice() throws NotValidOrderException, ManyParamsException, IOException {
        List<ProductDTO> ordered = productSFromFileOrderPrice();

        assertThat(ordered).isEqualTo(productRepository.listFilteredProducts(null,null,
                null,null,null,null,null,2));
    }

    @Test
    public void shouldReturnOrderedByPriceInverse() throws NotValidOrderException, ManyParamsException, IOException {
        List<ProductDTO> ordered = productSFromFileOrderPriceInverse();

        assertThat(ordered).isEqualTo(productRepository.listFilteredProducts(null,null,
                null,null,null,null,null,3));
    }


}
