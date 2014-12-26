package shoppinglist.com.shoppinglist;

import java.util.ArrayList;

/**
 * Created by hsperr on 12/26/14.
 */
public class DataLoader {

    ArrayList<ShoppingModel> data = new ArrayList<ShoppingModel>();
    DataLoader(){
        loadData();
    }

    private void loadData(){
        //TODO: add loading data from file
        data.add(new ShoppingModel("Karotten", false));
        data.add(new ShoppingModel("Salad", true));
    }

    ArrayList<ShoppingModel> getData(){
        return data;
    }

}
