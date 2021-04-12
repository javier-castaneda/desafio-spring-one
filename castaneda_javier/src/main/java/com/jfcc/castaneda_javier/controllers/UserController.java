package com.jfcc.castaneda_javier.controllers;

import com.jfcc.castaneda_javier.dto.*;
import com.jfcc.castaneda_javier.exceptions.*;
import com.jfcc.castaneda_javier.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

//Controlador para puntos 8-12 (con errores)
//usuarios registrados están en
@RestController()
@RequestMapping("/api/v2")
public class UserController {

    @Autowired
    UserService userService;

    //Endpoint GET para listar los usuarios registrados en la base de datos
    // (muestra solo email y usernames por "seguridad")
    @GetMapping("/all-users")
    public ResponseEntity<List<UserShowDTO>> showAllUsers() throws WrongUserDatabase, UserDatabaseNotFoundException {
        return new ResponseEntity<>(userService.listAll(), HttpStatus.OK);
    }


    //Endpoint POST para registrar un nuevo usuario en la base de datos
    /*El usuario se obtiene del request como un objeto de este estilo:
    {
    "email":"email@email.com",
    "userName":"username3214",
    "password":"lacontrasena"
    }
     */
    @PostMapping("/register")
    public ResponseEntity<StatusCodeDTO> registerUser(@RequestBody UserRegisterDTO user) throws UserExistsException, UserDatabaseNotFoundException, WrongUserDatabase {
        UserDTO newUser = new UserDTO();
        newUser.setUserName(user.getUserName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setPersonalCart(new CartDTO());

        if (userService.register(newUser)) {
            StatusCodeDTO status = new StatusCodeDTO(200, "Usuario agregado a la base de datos");
            return new ResponseEntity<>(status, HttpStatus.OK);
        }
        throw new UserExistsException();
    }

    //Endpoint POST para ingresar en el sistema a hacer compras
    /*las credenciales se obtienen del request como un objeto de este estilo:
    {
    "userName":"username3214",
    "password":"lacontrasena"
    }
     */
    @PostMapping("/login")
    public ResponseEntity<StatusCodeDTO> login(@RequestBody UserLoginDTO user) throws WrongCredentialsException, UserDatabaseNotFoundException, WrongUserDatabase {
        if (userService.login(user.getUserName(), user.getPassword())) {
            return new ResponseEntity<>(new StatusCodeDTO(200, "Bienvenido " + user.getUserName()), HttpStatus.OK);
        }
        throw new WrongCredentialsException();
    }


    //Permite cerrar la sesión iniciada
    @GetMapping("/logout")
    public ResponseEntity<StatusCodeDTO> logout() throws NotLoggedInException {
        userService.logout();
        return new ResponseEntity<>(new StatusCodeDTO(200, "Se cerró la sesión"), HttpStatus.OK);
    }

    //Permite agregar elementos al carrito de compras de el usuario que haya iniciado sesión
    /* Recibe una lista de articulos como de la siguiente manera:
    [
        {
            "productId":1,
            "name":"Desmalezadora",
            "brand":"Makita",
            "quantity":1
        },
        {
            "productId":5,
            "name":"Zapatillas Deportivas",
            "brand":"Nike",
            "quantity":2
        }
    ]
     */
    @PostMapping("/add-to-cart")
    public ResponseEntity<StatusCodeDTO> addToMyCart(@RequestBody List<ProductPurchaseDTO> productList) throws NotLoggedInException, NotEnoughQuantityException, NameNotFoundException, BrandNotFoundException, ProductIdNotValidException, IOException, WrongUserDatabase, UserDatabaseNotFoundException {
        CartDTO cart = new CartDTO();
        cart.setArticles(productList);
        userService.addToCart(cart);
        return new ResponseEntity<>(new StatusCodeDTO(200, "Se agragaron los productos al carrito correctamente, para efectuar la compra use checkout"), HttpStatus.OK);
    }


    //Hace la solicitud de compra con el carrito del usuario que tiene la sesión iniciada
    //Modifica la base de datos de productos
    //A veces se cierra sesión
    @GetMapping("/checkout")
    public ResponseEntity<ResponsePurchaseDTO> checkout() throws NameNotFoundException, BrandNotFoundException, NotLoggedInException, IOException, ProductIdNotValidException, NotEnoughQuantityException, WrongUserDatabase, UserDatabaseNotFoundException {
        return new ResponseEntity<>(userService.checkout(), HttpStatus.OK);
    }

    //Permite verificar el carrito de compra antes de dar checkout
    @GetMapping("/watch-cart")
    public ResponseEntity<CartDTO> watchCart() throws NotLoggedInException, WrongUserDatabase, UserDatabaseNotFoundException {
        return new ResponseEntity<>(userService.checkCart(), HttpStatus.OK);
    }


    @ExceptionHandler(UserDatabaseNotFoundException.class)
    public ResponseEntity<StatusCodeDTO> badUserDatabase(UserDatabaseNotFoundException exception) {
        return new ResponseEntity<>(new StatusCodeDTO(500, "No se pudo cargar la base de datos de usuarios"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<StatusCodeDTO> userExists(UserExistsException exception) {
        return new ResponseEntity<>(new StatusCodeDTO(400, "El nombre de usuario o email ya existen, por favor intenta con uno diferente o ingresa usando login"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotLoggedInException.class)
    public ResponseEntity<StatusCodeDTO> notLoggedIn(NotLoggedInException exception) {
        return new ResponseEntity<>(new StatusCodeDTO(401, "Debes iniciar sesión para realizar la acción deseada"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AlreadyLoggedInException.class)
    public ResponseEntity<StatusCodeDTO> alreadyIn(AlreadyLoggedInException exception) {
        return new ResponseEntity<>(new StatusCodeDTO(400, "No puedes iniciar más de una sesión al tiempo, usa logout para cambiar de sesión"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<StatusCodeDTO> wrongCredentials(WrongCredentialsException exception) {
        return new ResponseEntity<>(new StatusCodeDTO(401, "Credenciales de inicio de sesión incorrectas"), HttpStatus.UNAUTHORIZED);
    }

    //Excepción para cuando se pone un tipo de orden de artículos que no existe
    @ExceptionHandler(NotValidOrderException.class)
    public ResponseEntity<StatusCodeDTO> notValidOrderHandler(NotValidOrderException exception) {
        StatusCodeDTO error = new StatusCodeDTO(400, "El tipo de orden " + exception.getMessage() + " no existe, usa un número entero entre 0 and 3");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //Excepción para cuando se intenta comprar un elemento con un id que no está en la bd
    @ExceptionHandler(ProductIdNotValidException.class)
    public ResponseEntity<ResponsePurchaseDTO> notValidId(ProductIdNotValidException exception) {
        ResponsePurchaseDTO response = new ResponsePurchaseDTO();
        response.setStatusCode(new StatusCodeDTO(400, "El id " + exception.getMessage() + " no existe, cambie el id para realizar la compra"));
        response.setTicket(new TicketFailDTO(exception.getCart().getArticles()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Excepción para cuando se intenta comprar un elemento con un nombre de producto
    //que no está en la bd o no corresponde al Id
    @ExceptionHandler(NameNotFoundException.class)
    public ResponseEntity<ResponsePurchaseDTO> notValidName(NameNotFoundException exception) {
        ResponsePurchaseDTO response = new ResponsePurchaseDTO();
        response.setStatusCode(new StatusCodeDTO(400, "El artículo de nombre " + exception.getMessage() + " no existe, revisa que el nombre sea el correcto para realizar la compra"));
        response.setTicket(new TicketFailDTO(exception.getCart().getArticles()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Excepción para cuando se intenta comprar un elemento con una marca de producto
    //que no está en la bd o no corresponde al Id y nombre
    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<ResponsePurchaseDTO> notValidBrand(BrandNotFoundException exception) {
        ResponsePurchaseDTO response = new ResponsePurchaseDTO();
        response.setStatusCode(new StatusCodeDTO(400, "El artículo " + exception.getMessage() + " no existe, revisa que la marca sea correcta para realizar la compra"));
        response.setTicket(new TicketFailDTO(exception.getCart().getArticles()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Excepción para cuando se intenta comprar un elemento y no hay suficiente Stock
    @ExceptionHandler(NotEnoughQuantityException.class)
    public ResponseEntity<ResponsePurchaseDTO> notEnough(NotEnoughQuantityException exception) {
        ResponsePurchaseDTO response = new ResponsePurchaseDTO();
        response.setStatusCode(new StatusCodeDTO(400, "Del artículo " + exception.getMessage() + " quedan " + exception.getQuantity() + " unidades disponibles"));
        response.setTicket(new TicketFailDTO(exception.getCart().getArticles()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
