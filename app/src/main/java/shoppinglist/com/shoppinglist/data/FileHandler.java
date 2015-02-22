package shoppinglist.com.shoppinglist.data;

import android.content.Context;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import shoppinglist.com.shoppinglist.R;
import shoppinglist.com.shoppinglist.ShoppingModel;

public class FileHandler implements DataLoader, DataWriter{
    public ArrayList<ShoppingModel> readDataFile(Context context) {
        Scanner s = null;
        String filename = context.getString(R.string.filename);

        ArrayList<ShoppingModel> list = new ArrayList<ShoppingModel>();
        try {
            s = new Scanner(new File(context.getFilesDir(), filename));
            while (s.hasNext()){
                String name = s.next();
                boolean bought = Boolean.parseBoolean(s.next());
                list.add(new ShoppingModel(name, bought));
            }
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void writeData(ArrayList<ShoppingModel> data, Context c) {
        String filename = c.getString(R.string.filename);
        File file = new File(c.getFilesDir(), filename);

        FileOutputStream fout = null;
        Log.d("WriteData", c.getFilesDir().getAbsolutePath());
        try{
            fout = c.openFileOutput(filename, c.MODE_PRIVATE);
            OutputStreamWriter fosw = new OutputStreamWriter(fout);
            BufferedWriter bwriter = new BufferedWriter(fosw);

            for(ShoppingModel model : data){
                bwriter.write(model.getName()+" "+model.isBought());
                bwriter.newLine();
            }
            bwriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

