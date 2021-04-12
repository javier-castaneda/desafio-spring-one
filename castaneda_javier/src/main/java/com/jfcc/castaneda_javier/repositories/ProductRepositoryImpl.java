package com.jfcc.castaneda_javier.repositories;

import com.jfcc.castaneda_javier.dto.*;
import com.jfcc.castaneda_javier.exceptions.*;
import com.jfcc.castaneda_javier.repositories.comparators.NameAsc;
import com.jfcc.castaneda_javier.repositories.comparators.NameDesc;
import com.jfcc.castaneda_javier.repositories.comparators.PriceAsc;
import com.jfcc.castaneda_javier.repositories.comparators.PriceDesc;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private static int actualTicket = 0;
    private List<ProductDTO> productList = null;


    //Método para listar todos los elementos del CSV como ProductDTO
    @Override
    public List<ProductDTO> listAllProducts() throws IOException {
        if (productList==null)
            productList=loadDatabase();

        return productList;
    }


    //Método que retorna la lista de los elementos filtrada por los parámetros que se le pasen y ordenada según se requiera
    //Métodos de orden:
    //0: Orden alfabético
    //1: Orden alfabético inverso
    //2: Orden por precio - mayora a menor
    //3: Orden por precio - menor a mayor
    @Override
    public List<ProductDTO> listFilteredProducts(Integer productId, String name, String category, String brand, Boolean freeShipping, Integer prestige, Double price, Integer order) throws IOException, ManyParamsException, NotValidOrderException {
        int params = 0;
        List<ProductDTO> productDTOS = listAllProducts();
        if (category != null) {
            productDTOS = productDTOS.stream().filter(productDTO -> productDTO.getCategory().equals(category)).collect(Collectors.toList());
            params++;
        }
        if (productId != null) {
            productDTOS = productDTOS.stream().filter(productDTO -> productDTO.getProductId() == productId).collect(Collectors.toList());
            params++;
        }
        if (name != null) {
            productDTOS = productDTOS.stream().filter(productDTO -> productDTO.getProduct().equals(name)).collect(Collectors.toList());
            params++;
        }
        if (brand != null) {
            productDTOS = productDTOS.stream().filter(productDTO -> productDTO.getBrand().equals(brand)).collect(Collectors.toList());
            params++;
        }
        if (freeShipping != null) {
            params++;
            if (freeShipping)
                productDTOS = productDTOS.stream().filter(productDTO -> productDTO.isFreeShipping()).collect(Collectors.toList());
            else
                productDTOS = productDTOS.stream().filter(productDTO -> !productDTO.isFreeShipping()).collect(Collectors.toList());
        }
        if (price != null) {
            productDTOS = productDTOS.stream().filter(productDTO -> productDTO.getPrice() == price).collect(Collectors.toList());
            params++;
        }
        if (prestige != null) {
            productDTOS = productDTOS.stream().filter(productDTO -> productDTO.getPrestige() == prestige).collect(Collectors.toList());
            params++;
        }

        if (params > 2)
            throw new ManyParamsException(String.valueOf(params));

        if (order != null) {

            switch (order) {
                case 0:
                    sort(productDTOS, new NameAsc());
                    break;
                case 1:
                    sort(productDTOS, new NameDesc());
                    break;
                case 2:
                    sort(productDTOS, new PriceDesc());
                    break;
                case 3:
                    sort(productDTOS, new PriceAsc());
                    break;
                default:
                    throw new NotValidOrderException(Integer.toString(order));
            }
        }

        return productDTOS;
    }

    //Verifica que los datos de la compra sean correctos
    //y en caso que no sea así hace throw de la excepción correspondiente
    @Override
    public boolean productAvailable(ProductPurchaseDTO prod) throws IOException, ProductIdNotValidException, NameNotFoundException, BrandNotFoundException, NotEnoughQuantityException {
        List<ProductDTO> productDTOS = listAllProducts();
        productDTOS = productDTOS.stream().filter(productDTO -> productDTO.getProductId() == prod.getProductId()).collect(Collectors.toList());
        if (productDTOS.size() < 1)
            throw new ProductIdNotValidException(String.valueOf(prod.getProductId()));
        productDTOS = productDTOS.stream().filter(productDTO -> productDTO.getProduct().equals(prod.getName())).collect(Collectors.toList());
        if (productDTOS.size() < 1)
            throw new NameNotFoundException(prod.getName());
        productDTOS = productDTOS.stream().filter(productDTO -> productDTO.getBrand().equals(prod.getBrand())).collect(Collectors.toList());
        if (productDTOS.size() < 1)
            throw new BrandNotFoundException(prod.getName() + " " + prod.getBrand());
        if (productDTOS.get(0).getQuantity() < prod.getQuantity())
            throw new NotEnoughQuantityException(prod.getName() + " " + prod.getBrand(), productDTOS.get(0).getQuantity());

        return productDTOS.size() == 1;
    }


    //Método que comprueba llama al método productAvailable para levantar una excepcion
    //con el carrito de compra entre sus parámetros si es el caso ó retorna un StatusCode
    //para la creación de un ResponsePurchaseDTO si all es correcto
    @Override
    public StatusCodeDTO checkAviability(CartDTO cart) throws IOException, ProductIdNotValidException, NameNotFoundException, BrandNotFoundException, NotEnoughQuantityException {

        for (ProductPurchaseDTO prod : cart.getArticles()) {
            try {
                if (productAvailable(prod)) {

                }
            } catch (ProductIdNotValidException exception) {
                throw new ProductIdNotValidException(exception.getMessage(), cart);
            } catch (NameNotFoundException e) {
                throw new NameNotFoundException(e.getMessage(), cart);
            } catch (BrandNotFoundException e) {
                throw new BrandNotFoundException(e.getMessage(), cart);
            } catch (NotEnoughQuantityException e) {
                throw new NotEnoughQuantityException(e.getMessage(), e.getQuantity(), cart);
            }
        }

        return new StatusCodeDTO(200, "La solicitud de compra se completó con éxito");
    }


    //Método que reduce la cantidad de elementos disponibles en el inventario y luego retorna un ticket con los detalles
    @Override
    public TicketOkDTO makePurchase(CartDTO cart) throws IOException {
        makeBuy(cart);
        actualTicket++;
        return new TicketOkDTO(cart,actualTicket, makeTotal(cart));
    }


    //Método que carga todos los elementos que se encuentran en el CSV
    private List<ProductDTO> loadDatabase() throws IOException {
        int productId = 0;
        List<ProductDTO> productDTOS = new ArrayList<>();
        String line = "";
        FileReader fr = new FileReader(getClass().getClassLoader().getResource("dbProductos.csv").getFile());
        BufferedReader br = new BufferedReader(fr);
        while ((line = br.readLine()) != null) {
            if (productId > 0) {
                ProductDTO product = new ProductDTO();
                String[] dbprod = line.split(",");
                product.setProductId(productId);
                product.setProduct(dbprod[0]);
                product.setCategory(dbprod[1]);
                product.setBrand(dbprod[2]);
                product.setPrice(Long.parseLong(dbprod[3].replace("$", "").replace(".", "")));
                product.setQuantity(Integer.parseInt(dbprod[4]));
                if (dbprod[5].equals("SI"))
                    product.setFreeShipping(true);
                else
                    product.setFreeShipping(false);

                product.setPrestige(StringUtils.countOccurrencesOf(dbprod[6], "*"));
                productDTOS.add(product);
            }
            productId++;
        }
        br.close();
        fr.close();
        return productDTOS;
    }

    //Método para ordenar según el tipo de comparador que se le pase
    //Los comparadores están en el paquete repositories.comparators
    private void sort(List<ProductDTO> list, Comparator<ProductDTO> comparator) {
        quicksort(list, 0, list.size() - 1, comparator);
    }


    //Implementación de QuickSort usando la interface Comparator
    private void quicksort(List<ProductDTO> A, int izq, int der, Comparator<ProductDTO> c) {

        ProductDTO pivote = A.get(izq);
        int i = izq;
        int j = der;
        ProductDTO aux;

        while (i < j) {
            while (c.compare(A.get(i), pivote) <= 0 && i < j) i++;
            while (c.compare(A.get(j), pivote) > 0) j--;
            if (i < j) {
                aux = A.get(i);
                A.set(i, A.get(j));
                A.set(j, aux);
            }
        }
        A.set(izq, A.get(j));
        A.set(j, pivote);

        if (izq < j - 1)
            quicksort(A, izq, j - 1, c);
        if (j + 1 < der)
            quicksort(A, j + 1, der, c);

    }

    //Método para calcular el total de la compra
    private long makeTotal(CartDTO cart) throws IOException {
        long total = 0;
        List<ProductDTO> productDTOS = listAllProducts();
        for (ProductPurchaseDTO prod:cart.getArticles()) {
            ProductDTO buying = productDTOS.stream().filter(productDTO -> productDTO.getProductId() == prod.getProductId()).collect(Collectors.toList()).get(0);
            total += prod.getQuantity()* buying.getPrice();
        }
        return total;
    }


    //Método que realiza los cambios en la lista de productDTO de la clase
    private void makeBuy(CartDTO cart) throws IOException {
        List<ProductDTO> productDTOS = listAllProducts();
        for (ProductPurchaseDTO prod:cart.getArticles()) {
            ProductDTO buying = productDTOS.stream().filter(productDTO -> productDTO.getProductId() == prod.getProductId()).collect(Collectors.toList()).get(0);
            buying.setQuantity(buying.getQuantity()-prod.getQuantity());
        }
        updateDatabase(productDTOS);
    }

    private void updateDatabase(List<ProductDTO> actualList) throws IOException {

        String productStrings = "name,category,brand,price,quantity,freeShipping,prestige"+System.lineSeparator();
        for (ProductDTO prod:actualList) {
            productStrings+=toLine(prod);
        }

        FileWriter fw = new FileWriter(getClass().getClassLoader().getResource("dbProductos.csv").getFile());
        fw.write(productStrings);

        fw.close();
    }

    private String toLine(ProductDTO prod){
        String line = "";
        line+=prod.getProduct();
        line+=","+prod.getCategory();
        line+=","+prod.getBrand();
        line+=",$"+prod.getPrice();
        line+=","+prod.getQuantity();
        if(prod.isFreeShipping())
            line+=",SI";
        else
            line+=",NO";
        String prestige= "";
        for (int i = 0; i < prod.getPrestige(); i++) {
            prestige+="*";
        }
        line+=","+prestige;
        line +=System.lineSeparator();
        System.out.println(line);
        return line;

    }


}
