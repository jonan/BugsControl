package jp.bugscontrol.server;

import java.util.ArrayList;
import java.util.List;

import jp.bugscontrol.AdapterBug;
import jp.bugscontrol.AdapterProduct;

public abstract class Server {
    protected List<Product> products;
    AdapterProduct adapter_product;
    AdapterBug adapter_bug;

    public Server() {
        products = new ArrayList<Product>();
    }

    protected abstract void loadProducts();
    protected abstract void loadBugsForProduct(Product p);

    public void setAdapterProduct(AdapterProduct adapter) {
        adapter_product = adapter;
        loadProducts();
    }

    public void setAdapterBug(Product product, AdapterBug adapter) {
        adapter_bug = adapter;
        loadBugsForProduct(product);
    }

    public List<Product> getProducts() {return products;}

    protected void productsListUpdated() {
        adapter_product.notifyDataSetChanged();
    }

    protected void bugsListUpdated() {
        adapter_bug.notifyDataSetChanged();
    }

    public Product getProductFromId(int product_id) {
        for (Product p : products)
            if (p.getId() == product_id)
                return p;
        return null;
    }
}
