package shoppinglist.com.shoppinglist.data;

import android.content.Context;

import java.util.ArrayList;

import shoppinglist.com.shoppinglist.ShoppingModel;

public class DummyLoader implements DataLoader{

    @Override
    public ArrayList<ShoppingModel> readDataFile(Context context) {
        ArrayList<ShoppingModel> list = new ArrayList<ShoppingModel>();
        list.add(new ShoppingModel("Karotten", false));
        list.add(new ShoppingModel("Salad", true));
        list.add(new ShoppingModel("Bread", false));
        list.add(new ShoppingModel("Butter", false));
        return list;
    }
}
