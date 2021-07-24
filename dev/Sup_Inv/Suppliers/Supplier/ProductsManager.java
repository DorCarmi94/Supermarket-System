package Sup_Inv.Suppliers.Supplier;

import Sup_Inv.DataAccess.SupInvDBConn;
import Sup_Inv.Suppliers.DataAccess.ProductMapper;

import java.util.List;

public class ProductsManager {

    private static ProductsManager instance = null;

    private ProductMapper productMapper;
    //TODO remove it or use it to hold upto 100 products.
    //private Map<Integer, Product> productMap; //BarcodeToProductObject

    private ProductsManager(){
        //productMap = new HashMap<>();
        productMapper = new ProductMapper(SupInvDBConn.getInstance().getConn());
    }

    public static ProductsManager getInstance(){
        if(instance == null){
            instance = new ProductsManager();
        }
        return instance;
    }

    public boolean addIfAbsent(Product product){

        return productMapper.insert(product) > -1;


        /*if(!productMap.containsKey(addProduct.barCode))
        {
            productMap.put(addProduct.barCode,new Product(addProduct.barCode,addProduct.manufacture,addProduct.name));
            return true;
        }
        else
            return false;*/
    }

    public List<Integer> getAllBarcodes(){
        return productMapper.getAllBarcodes();
    }

    public boolean exists(int barcode){
        return productMapper.findById(barcode) != null;
    }

    public Product getProduct(int barcode) {
        return productMapper.findById(barcode);
    }

    public void updateIfNeededFreqSupplyAndMinPrice(int barCode, int freqSupply, double minPrice) {
        boolean update = false;
        Product product = getProduct(barCode);
        if(product == null){
            return;
        }

        if(product.getFreqSupply() > freqSupply){
            product.setFreqSupply(freqSupply);
            update = true;
        }

        if(product.getMinPrice() > minPrice){
            update = true;
            product.setMinPrice(minPrice);
        }

        if(update){
            productMapper.update(product);
        }
    }
}
