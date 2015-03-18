package shoppinglist.com.shoppinglist;

/**
 * Created by hsperr on 12/26/14.
 */
public class ShoppingModel {
    String name;
    boolean bought;

    public ShoppingModel(String name, boolean bought) {
        this.name = name;
        this.bought = bought;
    }

    public String getName() {
        return name;
    }

    public boolean isBought() {
        return bought;
    }
}
