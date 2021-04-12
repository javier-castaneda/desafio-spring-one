package com.jfcc.castaneda_javier;

import com.jfcc.castaneda_javier.controllers.MarketController;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.jfcc.castaneda_javier.productlists.ProductListTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MarketControllerTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private MarketController marketController;

    @BeforeEach
    public void setUp(){
        productRepository = mock(ProductRepositoryImpl.class);
        productService = new ProductServiceImpl(productRepository);
        marketController = new MarketController(productService);

    }

    @Test
    public void shouldGetAllProducts() throws IOException, ManyParamsException, NotValidOrderException {
        List<ProductDTO> products = createFourProducts();
        when(productRepository.listFilteredProducts(null,null,null,null,null,
                null,null,null)).thenReturn(products);

        List<ProductShowDTO> returnedProducts = marketController.getProducts(null,null,null,
                null,null,null,null,null);

        verify(productRepository,atLeast(1)).listFilteredProducts(null,null,
                null,null,null,null,null,null);
        assertThat(returnedProducts).isEqualTo(fourProductShow());
    }

    @Test
    public void shouldGetTools() throws NotValidOrderException, ManyParamsException, IOException {
        List<ProductDTO> products = createFourProducts();
        List<ProductDTO> filtered = products.stream().filter(productDTO->productDTO.getCategory()=="Herramienta").collect(Collectors.toList());
        when(productRepository.listFilteredProducts(null,null,"Herramienta",null,
                null,null,null,null)).thenReturn(filtered);

        List<ProductShowDTO> returned = marketController.getProducts(null,null,"Herramienta",
                null,null,null,null,null);

        verify(productRepository,atLeast(1)).listFilteredProducts(null,null,
                "Herramienta",null,null,null,null,null);
        assertThat(returned).isEqualTo(fourProductShow().stream().filter(productShowDTO -> productShowDTO
                .getCategory()=="Herramienta").collect(Collectors.toList()));
    }

    @Test
    public void shouldGetToolsFreeShipping() throws NotValidOrderException, ManyParamsException, IOException {
        List<ProductDTO> products = createEightProducts();
        List<ProductDTO> filtered = products.stream().filter(productDTO->productDTO.getCategory()=="Herramienta").filter(productDTO -> productDTO.isFreeShipping()).collect(Collectors.toList());
        when(productRepository.listFilteredProducts(null,null,"Herramienta",null,
                true,null,null,null)).thenReturn(filtered);

        List<ProductShowDTO> returned = marketController.getProducts(null,null,"Herramienta",
                null,null,true,null,null);

        verify(productRepository,atLeast(1)).listFilteredProducts(null,null,"Herramienta",null,
                true,null,null,null);

        assertThat(returned).isEqualTo(filteredProductShow());
    }

    @Test
    public void shouldOrderAlphabetic() throws IOException, ManyParamsException, NotValidOrderException {
        List<ProductDTO> products = createFourProductsAscOrder();
        when(productRepository.listFilteredProducts(null, null, null,
                null,null,null,null,0)).thenReturn(products);

        List<ProductShowDTO> returned = marketController.getProducts(null, null, null,
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

        List<ProductShowDTO> returned = marketController.getProducts(null, null, null,
                null,null,null,null,1);


        verify(productRepository,atLeast(1)).listFilteredProducts(null, null, null,
                null,null,null,null,1);

        assertThat(returned).isEqualTo(fourProductDescOrderShow());
    }

    @Test
    public void shouldOrderByPrice() throws IOException, ManyParamsException, NotValidOrderException {
        List<ProductDTO> products = createFourProductsPriceOrder();
        when(productRepository.listFilteredProducts(null, null, null,
                null,null,null,null,3)).thenReturn(products);

        List<ProductShowDTO> returned = marketController.getProducts(null, null, null,
                null,null,null,null,3);


        verify(productRepository,atLeast(1)).listFilteredProducts(null, null, null,
                null,null,null,null,3);

        assertThat(returned).isEqualTo(fourProductPriceOrderShow());
    }

    @Test
    public void shouldOrderByPriceInverse() throws IOException, ManyParamsException, NotValidOrderException {
        List<ProductDTO> products = createFourProductsPriceOrderInverse();
        when(productRepository.listFilteredProducts(null, null, null,
                null,null,null,null,2)).thenReturn(products);

        List<ProductShowDTO> returned = marketController.getProducts(null, null, null,
                null,null,null,null,2);


        verify(productRepository,atLeast(1)).listFilteredProducts(null, null, null,
                null,null,null,null,2);

        assertThat(returned).isEqualTo(fourProductPriceOrderInverseShow());
    }

    @Test
    public void makeBuyCorrect() throws IOException, NotEnoughQuantityException, NameNotFoundException, ProductIdNotValidException, BrandNotFoundException {
        List<ProductDTO> products= createFourProducts();

        CartDTO cart= new CartDTO();
        ProductPurchaseDTO product1= new ProductPurchaseDTO();
        product1.setProductId(1);
        product1.setName("Papas");
        product1.setBrand("Pringles");
        product1.setQuantity(5);

        ProductPurchaseDTO product2= new ProductPurchaseDTO();
        product2.setProductId(2);
        product2.setName("Zapatilla");
        product2.setBrand("Nike");
        product2.setQuantity(1);

        cart.getArticles().add(product1);
        cart.getArticles().add(product2);

        when(productRepository.listAllProducts()).thenReturn(products);
        when(productRepository.checkAviability(cart)).thenReturn(new StatusCodeDTO(200, "La solicitud de compra se completó con éxito"));
        when(productRepository.makePurchase(cart)).thenReturn(new TicketOkDTO(cart,1,32500));

        TicketOkDTO okTicket = new TicketOkDTO(cart,1,32500);
        ResponsePurchaseDTO responseDTO = new ResponsePurchaseDTO();
        responseDTO.setTicket(okTicket);
        responseDTO.setStatusCode(new StatusCodeDTO(200, "La solicitud de compra se completó con éxito"));

        assertThat(productService.makePurchase(cart)).isEqualTo(responseDTO);

    }

    @Test
    public void makeBuyWrong() throws IOException, NotEnoughQuantityException, NameNotFoundException, ProductIdNotValidException, BrandNotFoundException {
        List<ProductDTO> products= createFourProducts();

        CartDTO cart= new CartDTO();
        ProductPurchaseDTO product1= new ProductPurchaseDTO();
        product1.setProductId(1);
        product1.setName("Papas");
        product1.setBrand("Pringles");
        product1.setQuantity(10);

        ProductPurchaseDTO product2= new ProductPurchaseDTO();
        product2.setProductId(2);
        product2.setName("Zapatilla");
        product2.setBrand("Nike");
        product2.setQuantity(10);

        cart.getArticles().add(product1);
        cart.getArticles().add(product2);

        when(productRepository.listAllProducts()).thenReturn(products);
        when(productRepository.checkAviability(cart)).thenThrow(new NotEnoughQuantityException("Zapatilla Nike",7,cart));
        when(productRepository.makePurchase(cart)).thenThrow(new NotEnoughQuantityException("Zapatilla Nike",7,cart));

        TicketOkDTO okTicket = new TicketOkDTO(cart,1,32500);
        ResponsePurchaseDTO responseDTO = new ResponsePurchaseDTO();
        responseDTO.setTicket(okTicket);
        responseDTO.setStatusCode(new StatusCodeDTO(200, "La solicitud de compra se completó con éxito"));

        assertThat(productService.makePurchase(cart)).isEqualTo(responseDTO);

    }

}
