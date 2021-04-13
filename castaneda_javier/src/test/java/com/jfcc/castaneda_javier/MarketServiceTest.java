package com.jfcc.castaneda_javier;

import com.jfcc.castaneda_javier.dto.*;
import com.jfcc.castaneda_javier.exceptions.*;
import com.jfcc.castaneda_javier.repositories.ProductRepository;
import com.jfcc.castaneda_javier.repositories.ProductRepositoryImpl;
import com.jfcc.castaneda_javier.services.ProductService;
import com.jfcc.castaneda_javier.services.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.jfcc.castaneda_javier.productlists.ProductListTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MarketServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp(){
        productRepository = mock(ProductRepositoryImpl.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    public void shouldGetAllProducts() throws IOException {
        List<ProductDTO> products = createFourProducts();
        when(productRepository.listAllProducts()).thenReturn(products);

        List<ProductShowDTO> result = productService.getAllProducts();

        verify(productRepository,atLeast(1)).listAllProducts();
        assertThat(result).isEqualTo(fourProductShow());
    }

    @Test
    public void shouldGetTools() throws NotValidOrderException, ManyParamsException, IOException {
        List<ProductDTO> productsFiltered = filterFourProducts();
        when(productRepository.listFilteredProducts(null,null,"Herramienta",null,
                null,null,null,null)).thenReturn(productsFiltered);

        List<ProductShowDTO> result = productService.getFiltered(null,null,"Herramienta",
                null,null,null,null,null);

        verify(productRepository,atLeastOnce()).listFilteredProducts(null,null,"Herramienta",
                null,null,null,null,null);
        assertThat(result).isEqualTo(fourProductShow().stream().filter(productShowDTO -> productShowDTO
                .getCategory()=="Herramienta").collect(Collectors.toList()));

    }

    @Test
    public void shouldGetToolsFreeShipping() throws NotValidOrderException, ManyParamsException, IOException {
        List<ProductDTO> productsFiltered = createEightProducts().stream().filter(productDTO -> productDTO
                .getCategory()=="Herramienta").filter(productDTO -> productDTO.isFreeShipping())
                .collect(Collectors.toList());

        when(productRepository.listFilteredProducts(null,null,"Herramienta",null,true,
                null,null,null)).thenReturn(productsFiltered);

        List<ProductShowDTO> result = productService.getFiltered(null,null,"Herramienta",
                null,true,null, null,null);

        verify(productRepository,atLeastOnce()).listFilteredProducts(null,null,"Herramienta", null,
                true, null,null,null);
        assertThat(result).isEqualTo(filteredProductShow());
    }

    @Test
    public void shouldOrderAlphabetic() throws IOException, ManyParamsException, NotValidOrderException {
        List<ProductDTO> products = createFourProductsAscOrder();
        when(productRepository.listFilteredProducts(null, null, null,
                null,null,null,null,0)).thenReturn(products);

        List<ProductShowDTO> returned = productService.getFiltered(null, null,null,
                null,null,null,null,0);


        verify(productRepository,atLeast(1)).listFilteredProducts(null, null, null,
                null,null,null,null,0);

        assertThat(returned).isEqualTo(fourProductAscOrderShow());
    }

    @Test
    public void shouldOrderAlphabeticInverse() throws IOException, ManyParamsException, NotValidOrderException {
        List<ProductDTO> products = createFourProductsDescOrder();
        when(productRepository.listFilteredProducts(null, null, null,
                null,null,null,null,1)).thenReturn(products);

        List<ProductShowDTO> returned = productService.getFiltered(null, null, null,
                null,null,null,null,1);


        verify(productRepository,atLeast(1)).listFilteredProducts(null, null, null,
                null,null,null,null,1);

        assertThat(returned).isEqualTo(fourProductDescOrderShow());
    }

    @Test
    public void shouldOrderByPriceAsc() throws IOException, ManyParamsException, NotValidOrderException {
        List<ProductDTO> products = createFourProductsPriceOrder();
        when(productRepository.listFilteredProducts(null, null, null,
                null,null,null,null,2)).thenReturn(products);

        List<ProductShowDTO> returned = productService.getFiltered(null, null, null,
                null,null,null,null,2);


        verify(productRepository,atLeast(1)).listFilteredProducts(null, null, null,
                null,null,null,null,2);

        assertThat(returned).isEqualTo(fourProductPriceOrderShow());
    }

    @Test
    public void shouldOrderByPriceDesc() throws IOException, ManyParamsException, NotValidOrderException {
        List<ProductDTO> products = createFourProductsPriceOrderInverse();
        when(productRepository.listFilteredProducts(null, null, null,
                null,null,null,null,3)).thenReturn(products);

        List<ProductShowDTO> returned = productService.getFiltered(null, null, null,
                null,null,null,null,3);


        verify(productRepository,atLeast(1)).listFilteredProducts(null, null, null,
                null,null,null,null,3);

        assertThat(returned).isEqualTo(fourProductPriceOrderInverseShow());
    }

    @Test
    public void shouldReturnATicketOK() throws NotEnoughQuantityException, NameNotFoundException, IOException, ProductIdNotValidException, BrandNotFoundException {
        CartDTO cart = new CartDTO();
        List<ProductPurchaseDTO> products = new ArrayList<>();
        ProductPurchaseDTO prod1 = new ProductPurchaseDTO(1,"Papas","Pringles",3);
        ProductPurchaseDTO prod2 = new ProductPurchaseDTO(2,"Zapatilla","Nike",2);
        products.add(prod1);
        products.add(prod2);
        cart.setArticles(products);
        StatusCodeDTO status = new StatusCodeDTO(200, "La solicitud de compra se completó con éxito");
        TicketDTO ticket = new TicketOkDTO(cart,1,61500);

        when(productRepository.checkAviability(any())).thenReturn(status);
        when(productRepository.makePurchase(any())).thenReturn((TicketOkDTO) ticket);

        ResponsePurchaseDTO response = productService.makePurchase(cart);

        verify(productRepository,atLeastOnce()).checkAviability(any());
        verify(productRepository,atLeastOnce()).makePurchase(any());

        assertThat(response).isEqualTo(new ResponsePurchaseDTO(ticket, status));
    }

    @Test
    public void shouldReturnATicketBad() throws NotEnoughQuantityException, NameNotFoundException, IOException, ProductIdNotValidException, BrandNotFoundException {
        CartDTO cart = new CartDTO();
        List<ProductPurchaseDTO> products = new ArrayList<>();
        ProductPurchaseDTO prod1 = new ProductPurchaseDTO(1,"Papas","Pringles",200);
        ProductPurchaseDTO prod2 = new ProductPurchaseDTO(2,"Zapatilla","Nike",2);
        products.add(prod1);
        products.add(prod2);
        cart.setArticles(products);
        boolean exceptionJump = false;
        TicketDTO ticket = new TicketOkDTO(cart,1,61500);
        NotEnoughQuantityException e = new NotEnoughQuantityException("Papas Pringles",3,cart);

        when(productRepository.checkAviability(any())).thenThrow(e);
        when(productRepository.makePurchase(any())).thenReturn((TicketOkDTO) ticket);

        try {
            ResponsePurchaseDTO response = productService.makePurchase(cart);
        }catch (Exception exception ){
            exceptionJump = true;
        }
        verify(productRepository,atLeastOnce()).checkAviability(any());

        assertThat(exceptionJump).isTrue();
    }

}
