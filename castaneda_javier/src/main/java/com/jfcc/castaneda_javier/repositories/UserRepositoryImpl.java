package com.jfcc.castaneda_javier.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfcc.castaneda_javier.dto.CartDTO;
import com.jfcc.castaneda_javier.dto.ProductPurchaseDTO;
import com.jfcc.castaneda_javier.dto.UserDTO;
import com.jfcc.castaneda_javier.dto.UserShowDTO;
import com.jfcc.castaneda_javier.exceptions.UserDatabaseNotFoundException;
import com.jfcc.castaneda_javier.exceptions.UserExistsException;
import com.jfcc.castaneda_javier.exceptions.WrongCredentialsException;
import com.jfcc.castaneda_javier.exceptions.WrongUserDatabase;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository{

    private List<UserDTO> userList;

    @Override
    public List<UserShowDTO> showAllUsers() throws UserDatabaseNotFoundException, WrongUserDatabase {
        List<UserDTO> users= listUsers();
        List<UserShowDTO> userShowList = new ArrayList<>();

        for (UserDTO user:users) {
            UserShowDTO nuevo = new UserShowDTO();
            nuevo.setUserName(user.getUserName());
            nuevo.setEmail(user.getEmail());
            userShowList.add(nuevo);
        }

        return userShowList;
    }

    private List<UserDTO> listUsers() throws UserDatabaseNotFoundException, WrongUserDatabase {
        if(userList == null){
            userList = loadDatabase();
        }
        return userList;
    }

    private List<UserDTO> loadDatabase() throws UserDatabaseNotFoundException, WrongUserDatabase {
        File file = null;

        try {
            file = ResourceUtils.getFile("classpath:users.json");
        } catch (Exception e) {
            throw new UserDatabaseNotFoundException();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<UserDTO>> typeReference = new TypeReference<>() {};
        List<UserDTO> ingredientList = null;
        try {
            ingredientList = objectMapper.readValue(file, typeReference);
        } catch (Exception e) {
            throw new WrongUserDatabase();
        }
        return ingredientList;
    }

    @Override
    public boolean exists(String email, String userName) throws UserDatabaseNotFoundException, WrongUserDatabase {
        for (UserShowDTO user:showAllUsers()) {
            if(user.getUserName().equals(userName) || user.getEmail().equals(email))
                return true;
        }
        return false;
    }

    @Override
    public void addUser(UserDTO user) throws UserExistsException, UserDatabaseNotFoundException, WrongUserDatabase {
        if(!exists(user.getEmail(),user.getUserName())){
            userList.add(user);
            updateUserDatabase();
        }else{
            throw new UserExistsException();
        }
    }


    @Override
    public String addToUserCart(UserDTO user, CartDTO cart) {
        String userName = user.getUserName();
        List<Integer> in = new ArrayList<>();

        for (ProductPurchaseDTO prod: cart.getArticles()) {
            for (ProductPurchaseDTO prodUser:user.getPersonalCart().getArticles()) {
                if (prod.getProductId()==prodUser.getProductId()){
                    prodUser.setQuantity(prodUser.getQuantity()+prod.getQuantity());
                    in.add(prod.getProductId());
                }
            }
            if(!in.contains(prod.getProductId())){
                user.getPersonalCart().getArticles().add(prod);
            }
        }
        updateUserDatabase();
        return userName;
    }

    @Override
    public String clearCart(UserDTO user) {
        String userName = user.getUserName();
        user.getPersonalCart().setArticles(new ArrayList<>());
        updateUserDatabase();
        return userName;
    }

    @Override
    public UserDTO authenticate(String username, String password) throws WrongUserDatabase, UserDatabaseNotFoundException, WrongCredentialsException {

        List<UserDTO> userDTOList = listUsers();

        userDTOList = userDTOList.stream().filter(userDTO -> userDTO.getUserName().equals(username)).collect(Collectors.toList());
        userDTOList = userDTOList.stream().filter(userDTO -> userDTO.getPassword().equals(password)).collect(Collectors.toList());

        if (userDTOList.size()<1){
            throw new WrongCredentialsException();
        }

        return userDTOList.get(0);
    }

    @Override
    public UserDTO getByUserName(String userName) throws WrongUserDatabase, UserDatabaseNotFoundException {
        List<UserDTO> userDTOList = listUsers();
        return userDTOList.stream().filter(userDTO -> userDTO.getUserName().equals(userName)).collect(Collectors.toList()).get(0);
    }


    private void updateUserDatabase(){
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:users.json");
        }catch (Exception e){
            System.out.println("No se pudo cargar el archivo");
            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if(userList!=null)
                objectMapper.writeValue(file, userList);
        }catch (Exception e){
            System.out.println("No puedo escribir");
            e.printStackTrace();
        }
    }

}
