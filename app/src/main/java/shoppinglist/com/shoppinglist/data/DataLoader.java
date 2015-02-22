package shoppinglist.com.shoppinglist.data;

import android.content.Context;

import java.util.ArrayList;

import shoppinglist.com.shoppinglist.ShoppingModel;

/**
 * Created by hsperr on 12/26/14.
 */

public interface DataLoader{
    public ArrayList<ShoppingModel> readDataFile(Context context);
}
