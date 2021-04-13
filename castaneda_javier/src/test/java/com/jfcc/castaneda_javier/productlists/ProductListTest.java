package com.jfcc.castaneda_javier.productlists;

import com.jfcc.castaneda_javier.dto.ProductDTO;
import com.jfcc.castaneda_javier.dto.ProductShowDTO;

import java.util.ArrayList;
import java.util.List;

public class ProductListTest {

    public static List<ProductDTO> createFourProducts() {
        List<ProductDTO> prod = new ArrayList<>();

        ProductDTO product1 = new ProductDTO(1,"Papas", "Comida","Pringles", 500, 15, true, 3);
        ProductDTO product2 = new ProductDTO(2,"Zapatilla","Ropa", "Nike", 30000, 7,false,4);
        ProductDTO product3 = new ProductDTO(3,"Destornillador","Herramienta", "Paquita", 180, 6,true,5);
        ProductDTO product4 = new ProductDTO(4,"Martillo","Herramienta", "Klein", 800, 8,false,4);

        prod.add(product1);
        prod.add(product2);
        prod.add(product3);
        prod.add(product4);

        return prod;
    }

    public static List<ProductDTO> filterFourProducts() {
        List<ProductDTO> prod = new ArrayList<>();

        ProductDTO product3 = new ProductDTO(3,"Destornillador","Herramienta", "Paquita", 180, 6,true,5);
        ProductDTO product4 = new ProductDTO(4,"Martillo","Herramienta", "Klein", 800, 8,false,4);

        prod.add(product3);
        prod.add(product4);

        return prod;
    }

    public static List<ProductShowDTO> fourProductShow(){

        List<ProductShowDTO> prodShows = new ArrayList<>();

        ProductShowDTO product1 = new ProductShowDTO(1,"Papas","Comida","Pringles","$500",15,"SI","***");
        ProductShowDTO product2 = new ProductShowDTO(2,"Zapatilla","Ropa","Nike","$30000",7,"NO","****");
        ProductShowDTO product3 = new ProductShowDTO(3,"Destornillador","Herramienta","Paquita","$180",6,"SI","*****");
        ProductShowDTO product4 = new ProductShowDTO(4,"Martillo","Herramienta","Klein","$800",8,"NO","****");

        prodShows.add(product1);
        prodShows.add(product2);
        prodShows.add(product3);
        prodShows.add(product4);

        return prodShows;

    }

    public static List<ProductDTO> createEightProducts() {
        List<ProductDTO> prod = new ArrayList<>();

        ProductDTO product1 = new ProductDTO(1,"Papas", "Comida","Pringles", 500, 15, true, 3);
        ProductDTO product2 = new ProductDTO(2,"Zapatilla","Ropa", "Nike", 30000, 7,false,4);
        ProductDTO product3 = new ProductDTO(3,"Destornillador","Herramienta", "Paquita", 180, 6,true,5);
        ProductDTO product4 = new ProductDTO(4,"Martillo","Herramienta", "Klein", 800, 8,false,4);
        ProductDTO product5 = new ProductDTO(5,"Brocha", "Herramienta","Pintuco", 700, 25, true, 5);
        ProductDTO product6 = new ProductDTO(6,"Computador","Tecnología", "Apple", 300000, 8,false,4);
        ProductDTO product7 = new ProductDTO(7,"Salchicha","Comida", "Zenú", 200, 9,false,4);
        ProductDTO product8 = new ProductDTO(8,"Cuaderno","Escolar", "Norma", 100, 13,false,3);

        prod.add(product1);
        prod.add(product2);
        prod.add(product3);
        prod.add(product4);
        prod.add(product5);
        prod.add(product6);
        prod.add(product7);
        prod.add(product8);

        return prod;
    }

    public static List<ProductShowDTO> filteredProductShow(){

        List<ProductShowDTO> prodShows = new ArrayList<>();

        ProductShowDTO product3 = new ProductShowDTO(3,"Destornillador","Herramienta","Paquita","$180",6,"SI","*****");
        ProductShowDTO product5 = new ProductShowDTO(5,"Brocha","Herramienta","Pintuco","$700",25,"SI","*****");

        prodShows.add(product3);
        prodShows.add(product5);

        return prodShows;

    }

    public static List<ProductDTO> createFourProductsAscOrder() {
        List<ProductDTO> prod = new ArrayList<>();

        ProductDTO product1 = new ProductDTO(1,"Papas", "Comida","Pringles", 500, 15, true, 3);
        ProductDTO product2 = new ProductDTO(2,"Zapatilla","Ropa", "Nike", 30000, 7,false,4);
        ProductDTO product3 = new ProductDTO(3,"Destornillador","Herramienta", "Paquita", 180, 6,true,5);
        ProductDTO product4 = new ProductDTO(4,"Martillo","Herramienta", "Klein", 800, 8,false,4);

        prod.add(product3);
        prod.add(product4);
        prod.add(product1);
        prod.add(product2);

        return prod;
    }

    public static List<ProductShowDTO> fourProductAscOrderShow(){

        List<ProductShowDTO> prodShows = new ArrayList<>();

        ProductShowDTO product1 = new ProductShowDTO(1,"Papas","Comida","Pringles","$500",15,"SI","***");
        ProductShowDTO product2 = new ProductShowDTO(2,"Zapatilla","Ropa","Nike","$30000",7,"NO","****");
        ProductShowDTO product3 = new ProductShowDTO(3,"Destornillador","Herramienta","Paquita","$180",6,"SI","*****");
        ProductShowDTO product4 = new ProductShowDTO(4,"Martillo","Herramienta","Klein","$800",8,"NO","****");

        prodShows.add(product3);
        prodShows.add(product4);
        prodShows.add(product1);
        prodShows.add(product2);

        return prodShows;

    }

    public static List<ProductDTO> createFourProductsDescOrder() {
        List<ProductDTO> prod = new ArrayList<>();

        ProductDTO product1 = new ProductDTO(1,"Papas", "Comida","Pringles", 500, 15, true, 3);
        ProductDTO product2 = new ProductDTO(2,"Zapatilla","Ropa", "Nike", 30000, 7,false,4);
        ProductDTO product3 = new ProductDTO(3,"Destornillador","Herramienta", "Paquita", 180, 6,true,5);
        ProductDTO product4 = new ProductDTO(4,"Martillo","Herramienta", "Klein", 800, 8,false,4);

        prod.add(product2);
        prod.add(product1);
        prod.add(product4);
        prod.add(product3);

        return prod;
    }

    public static List<ProductShowDTO> fourProductDescOrderShow(){

        List<ProductShowDTO> prodShows = new ArrayList<>();

        ProductShowDTO product1 = new ProductShowDTO(1,"Papas","Comida","Pringles","$500",15,"SI","***");
        ProductShowDTO product2 = new ProductShowDTO(2,"Zapatilla","Ropa","Nike","$30000",7,"NO","****");
        ProductShowDTO product3 = new ProductShowDTO(3,"Destornillador","Herramienta","Paquita","$180",6,"SI","*****");
        ProductShowDTO product4 = new ProductShowDTO(4,"Martillo","Herramienta","Klein","$800",8,"NO","****");

        prodShows.add(product2);
        prodShows.add(product1);
        prodShows.add(product4);
        prodShows.add(product3);

        return prodShows;

    }

    public static List<ProductDTO> createFourProductsPriceOrder() {
        List<ProductDTO> prod = new ArrayList<>();

        ProductDTO product1 = new ProductDTO(1,"Papas", "Comida","Pringles", 500, 15, true, 3);
        ProductDTO product2 = new ProductDTO(2,"Zapatilla","Ropa", "Nike", 30000, 7,false,4);
        ProductDTO product3 = new ProductDTO(3,"Destornillador","Herramienta", "Paquita", 180, 6,true,5);
        ProductDTO product4 = new ProductDTO(4,"Martillo","Herramienta", "Klein", 800, 8,false,4);

        prod.add(product2);
        prod.add(product4);
        prod.add(product1);
        prod.add(product3);

        return prod;
    }

    public static List<ProductShowDTO> fourProductPriceOrderShow(){

        List<ProductShowDTO> prodShows = new ArrayList<>();

        ProductShowDTO product1 = new ProductShowDTO(1,"Papas","Comida","Pringles","$500",15,"SI","***");
        ProductShowDTO product2 = new ProductShowDTO(2,"Zapatilla","Ropa","Nike","$30000",7,"NO","****");
        ProductShowDTO product3 = new ProductShowDTO(3,"Destornillador","Herramienta","Paquita","$180",6,"SI","*****");
        ProductShowDTO product4 = new ProductShowDTO(4,"Martillo","Herramienta","Klein","$800",8,"NO","****");

        prodShows.add(product2);
        prodShows.add(product4);
        prodShows.add(product1);
        prodShows.add(product3);

        return prodShows;

    }

    public static List<ProductDTO> createFourProductsPriceOrderInverse() {
        List<ProductDTO> prod = new ArrayList<>();

        ProductDTO product1 = new ProductDTO(1,"Papas", "Comida","Pringles", 500, 15, true, 3);
        ProductDTO product2 = new ProductDTO(2,"Zapatilla","Ropa", "Nike", 30000, 7,false,4);
        ProductDTO product3 = new ProductDTO(3,"Destornillador","Herramienta", "Paquita", 180, 6,true,5);
        ProductDTO product4 = new ProductDTO(4,"Martillo","Herramienta", "Klein", 800, 8,false,4);

        prod.add(product3);
        prod.add(product1);
        prod.add(product4);
        prod.add(product2);

        return prod;
    }

    public static List<ProductShowDTO> fourProductPriceOrderInverseShow(){

        List<ProductShowDTO> prodShows = new ArrayList<>();

        ProductShowDTO product1 = new ProductShowDTO(1,"Papas","Comida","Pringles","$500",15,"SI","***");
        ProductShowDTO product2 = new ProductShowDTO(2,"Zapatilla","Ropa","Nike","$30000",7,"NO","****");
        ProductShowDTO product3 = new ProductShowDTO(3,"Destornillador","Herramienta","Paquita","$180",6,"SI","*****");
        ProductShowDTO product4 = new ProductShowDTO(4,"Martillo","Herramienta","Klein","$800",8,"NO","****");

        prodShows.add(product3);
        prodShows.add(product1);
        prodShows.add(product4);
        prodShows.add(product2);

        return prodShows;

    }

}
