package shoppinglist.com.shoppinglist.data;

import android.content.Context;

import java.util.ArrayList;

import shoppinglist.com.shoppinglist.ShoppingModel;

public interface DataWriter{
    public void writeData(ArrayList<ShoppingModel> data, Context c);
}
