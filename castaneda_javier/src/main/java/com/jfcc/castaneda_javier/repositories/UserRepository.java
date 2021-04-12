package com.jfcc.castaneda_javier.repositories;

import com.jfcc.castaneda_javier.dto.CartDTO;
import com.jfcc.castaneda_javier.dto.UserDTO;
import com.jfcc.castaneda_javier.dto.UserShowDTO;
import com.jfcc.castaneda_javier.exceptions.UserDatabaseNotFoundException;
import com.jfcc.castaneda_javier.exceptions.UserExistsException;
import com.jfcc.castaneda_javier.exceptions.WrongCredentialsException;
import com.jfcc.castaneda_javier.exceptions.WrongUserDatabase;

import java.util.List;

public interface UserRepository {

    public List<UserShowDTO> showAllUsers() throws UserDatabaseNotFoundException, WrongUserDatabase;
    public boolean exists(String email, String userName) throws UserDatabaseNotFoundException, WrongUserDatabase;
    public void addUser(UserDTO user) throws UserExistsException, UserDatabaseNotFoundException, WrongUserDatabase;
    public String addToUserCart(UserDTO user, CartDTO cart);
    public String clearCart(UserDTO user);
    public UserDTO authenticate(String username, String password) throws WrongUserDatabase, UserDatabaseNotFoundException, WrongCredentialsException;
    public UserDTO getByUserName(String userName) throws WrongUserDatabase, UserDatabaseNotFoundException;


}
