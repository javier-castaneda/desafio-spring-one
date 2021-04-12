package com.jfcc.castaneda_javier.services;


import com.jfcc.castaneda_javier.dto.*;
import com.jfcc.castaneda_javier.exceptions.*;
import com.jfcc.castaneda_javier.repositories.ProductRepository;
import com.jfcc.castaneda_javier.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productService;

    private UserDTO actualUser;
    private String lastUserName;

    //Lista usuarios registrados
    @Override
    public List<UserShowDTO> listAll() throws WrongUserDatabase, UserDatabaseNotFoundException {
        return userRepository.showAllUsers();
    }

    //Registra un usuario si no existe su username o su email
    @Override
    public boolean register(UserDTO user) throws WrongUserDatabase, UserDatabaseNotFoundException, UserExistsException {
        userRepository.addUser(user);
        return true;
    }

    //Realiza la compra y modifica los datos en la base de datos de productos
    @Override
    public ResponsePurchaseDTO checkout() throws NotLoggedInException, NotEnoughQuantityException, NameNotFoundException, IOException, ProductIdNotValidException, BrandNotFoundException, WrongUserDatabase, UserDatabaseNotFoundException {
        if (actualUser != null) {
            ResponsePurchaseDTO response;
            response = new ResponsePurchaseDTO(productService.makePurchase(actualUser.getPersonalCart()), new StatusCodeDTO(200, "Usuario " + actualUser.getUserName() + ", su solicitud de compra ha sido exitosa"));
            clearCart();
            reloadUser(lastUserName);
            return response;
        } else
            throw new NotLoggedInException();
    }

    //Guarda temporalmente al usuario que ingresa como el actual
    @Override
    public boolean login(String userName, String password) throws WrongCredentialsException, UserDatabaseNotFoundException, WrongUserDatabase {
        actualUser = userRepository.authenticate(userName, password);
        lastUserName = userName;
        return true;
    }

    //olvida la info el usuario temporal que tenía sesion abierta
    @Override
    public void logout() throws NotLoggedInException {
        if (actualUser != null) {
            actualUser = null;
            lastUserName = "";
        } else
            throw new NotLoggedInException();
    }

    //Agrega una lista de artículos al carrito del usuario
    @Override
    public boolean addToCart(CartDTO cart) throws NotLoggedInException, NotEnoughQuantityException, NameNotFoundException, IOException, ProductIdNotValidException, BrandNotFoundException, WrongUserDatabase, UserDatabaseNotFoundException {
        if (lastUserName != "" && lastUserName != null)
            reloadUser(lastUserName);
        if (actualUser != null) {
            productService.checkAviability(cart);
            reloadUser(userRepository.addToUserCart(actualUser, cart));
        } else
            throw new NotLoggedInException();
        return true;
    }

    @Override
    public void clearCart() throws NotLoggedInException, WrongUserDatabase, UserDatabaseNotFoundException {
        if (actualUser != null) {
            reloadUser(userRepository.clearCart(actualUser));
        } else
            throw new NotLoggedInException();
    }

    //Permite dar una vistazo al carrito
    @Override
    public CartDTO checkCart() throws NotLoggedInException, WrongUserDatabase, UserDatabaseNotFoundException {
        if (lastUserName != "" && lastUserName != null)
            reloadUser(lastUserName);
        if (actualUser != null)
            return actualUser.getPersonalCart();
        else
            throw new NotLoggedInException();
    }

    //Recarga el usuario desde el repositorio de usuarios
    private void reloadUser(String userName) throws WrongUserDatabase, UserDatabaseNotFoundException, NotLoggedInException {
        if (lastUserName != "" && lastUserName != null) {
            actualUser = userRepository.getByUserName(userName);
            lastUserName = actualUser.getUserName();
        } else
            throw new NotLoggedInException();
    }
}
