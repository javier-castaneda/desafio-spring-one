package com.jfcc.castaneda_javier.controllers;

import com.jfcc.castaneda_javier.dto.*;
import com.jfcc.castaneda_javier.exceptions.*;
import com.jfcc.castaneda_javier.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

//Controlador para puntos 1 a 7-8
//Se pueden hacer compras sin tener usuario registrado
@RestController()
@RequestMapping("/api/v1")
public class MarketController {

    ProductService productService;

    @Autowired
    public MarketController(ProductService productService){
        this.productService = productService;
    }

    //Endpoints


    //Endpoint get para obtener productos, tanto la lista completa como la lista filtrada u ordenada
    //Se pueden usar máximo 2 filtros y un tipo de órden entre 0 y 3
    //En caso de no encontrar nada, se retorna una lista vacía porque no se considera un error
    @GetMapping("/articles")
    public List<ProductShowDTO> getProducts(@RequestParam(value="product", required = false)String product,
                                            @RequestParam(value="productId", required = false)Integer productId,
                                            @RequestParam(value="category", required = false)String category,
                                            @RequestParam(value="brand", required = false)String brand,
                                            @RequestParam(value="price", required = false)Double price,
                                            @RequestParam(value = "freeShipping", required = false)Boolean freeShipping,
                                            @RequestParam(value="prestige", required = false)Integer prestige,
                                            @RequestParam(value="order", required = false)Integer order) throws IOException, ManyParamsException, NotValidOrderException {
        return productService.getFiltered(productId,product,category,brand,freeShipping,prestige,price, order);
    }


    //Endpoint post para hacer una petición de compra
    //Recibe un objeto (CartDTO) con una variable articles que es una lista de artículos
    //que tienen productId, name, brand y quantity.
    //Cuando se hacer compra, se modifica el archivo dbProductos.csv (en la carpeta target)
    @PostMapping("/purchase-request")
    public ResponseEntity<ResponsePurchaseDTO> purchase(@RequestBody CartDTO cart) throws NotEnoughQuantityException, NameNotFoundException, BrandNotFoundException, ProductIdNotValidException, IOException {
        return new ResponseEntity<>(productService.makePurchase(cart), HttpStatus.OK);
    }


    //Manejo de Excepciones


    //Excepción para cuando hay muchos filtros en el listado de productos
    @ExceptionHandler(ManyParamsException.class)
    public ResponseEntity<StatusCodeDTO> manyParamsHandler(ManyParamsException exception){
        StatusCodeDTO error = new StatusCodeDTO(400,"Solo puedes usar hasta 2 filtros, estás usando "+ exception.getMessage()+" filtros");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //Excepción para cuando se pone un tipo de orden de artículos que no existe
    @ExceptionHandler(NotValidOrderException.class)
    public ResponseEntity<StatusCodeDTO> notValidOrderHandler(NotValidOrderException exception){
        StatusCodeDTO error = new StatusCodeDTO(400,"El tipo de orden "+ exception.getMessage()+" no existe, usa un número entero entre 0 and 3");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //Excepción para cuando se intenta comprar un elemento con un id que no está en la bd
    @ExceptionHandler(ProductIdNotValidException.class)
    public ResponseEntity<ResponsePurchaseDTO> notValidId(ProductIdNotValidException exception){
        ResponsePurchaseDTO response = new ResponsePurchaseDTO();
        response.setStatusCode(new StatusCodeDTO(400, "El id "+exception.getMessage()+" no existe, cambie el id para realizar la compra"));
        response.setTicket(new TicketFailDTO(exception.getCart().getArticles()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Excepción para cuando se intenta comprar un elemento con un nombre de producto
    //que no está en la bd o no corresponde al Id
    @ExceptionHandler(NameNotFoundException.class)
    public ResponseEntity<ResponsePurchaseDTO> notValidName(NameNotFoundException exception){
        ResponsePurchaseDTO response = new ResponsePurchaseDTO();
        response.setStatusCode(new StatusCodeDTO(400, "El artículo de nombre "+exception.getMessage()+" no existe, revisa que el nombre sea el correcto para realizar la compra"));
        response.setTicket(new TicketFailDTO(exception.getCart().getArticles()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Excepción para cuando se intenta comprar un elemento con una marca de producto
    //que no está en la bd o no corresponde al Id y nombre
    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<ResponsePurchaseDTO> notValidBrand(BrandNotFoundException exception){
        ResponsePurchaseDTO response = new ResponsePurchaseDTO();
        response.setStatusCode(new StatusCodeDTO(400, "El artículo "+exception.getMessage()+" no existe, revisa que la marca sea correcta para realizar la compra"));
        response.setTicket(new TicketFailDTO(exception.getCart().getArticles()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Excepción para cuando se intenta comprar un elemento y no hay suficiente Stock
    @ExceptionHandler(NotEnoughQuantityException.class)
    public ResponseEntity<ResponsePurchaseDTO> notEnough(NotEnoughQuantityException exception){
        ResponsePurchaseDTO response = new ResponsePurchaseDTO();
        response.setStatusCode(new StatusCodeDTO(400, "Del artículo "+exception.getMessage()+" quedan "+ exception.getQuantity()+" unidades disponibles"));
        response.setTicket(new TicketFailDTO(exception.getCart().getArticles()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Excepción para cuando no se puede cargar la base de datos (archivo no encotrado o ese tipo de problemas)
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorDTO> badDataBase(IOException exception){
        ErrorDTO error = new ErrorDTO();
        error.setName("Error al cargar la base de datos");
        error.setMessage("No se encontró el archivo o no se pudo cargar los elementos del mismo");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
}
