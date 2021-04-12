package com.jfcc.castaneda_javier.services;

import com.jfcc.castaneda_javier.dto.*;
import com.jfcc.castaneda_javier.exceptions.*;

import java.io.IOException;
import java.util.List;

public interface UserService {

    public List<UserShowDTO> listAll() throws WrongUserDatabase, UserDatabaseNotFoundException;
    public boolean register(UserDTO user) throws WrongUserDatabase, UserDatabaseNotFoundException, UserExistsException;
    public ResponsePurchaseDTO checkout() throws NotLoggedInException, NotEnoughQuantityException, NameNotFoundException, IOException, ProductIdNotValidException, BrandNotFoundException, WrongUserDatabase, UserDatabaseNotFoundException;
    public boolean login(String userName, String password) throws WrongCredentialsException, UserDatabaseNotFoundException, WrongUserDatabase;
    public void logout() throws NotLoggedInException;
    public boolean addToCart(CartDTO cart) throws NotLoggedInException, NotEnoughQuantityException, NameNotFoundException, IOException, ProductIdNotValidException, BrandNotFoundException, WrongUserDatabase, UserDatabaseNotFoundException;
    public void clearCart() throws NotLoggedInException, WrongUserDatabase, UserDatabaseNotFoundException;
    public CartDTO checkCart() throws NotLoggedInException, WrongUserDatabase, UserDatabaseNotFoundException;

}
