package shoppinglist.com.shoppinglist;

import java.util.ArrayList;

/**
 * Created by hsperr on 12/26/14.
 */
public class DataLoader {

    ArrayList<String> data = new ArrayList<String>();
    DataLoader(){
        loadData();
    }

    private void loadData(){
        //TODO: add loading data from file
        data.add("Karotten");
        data.add("Salat");
    }

    ArrayList<String> getData(){
        return data;
    }

}
