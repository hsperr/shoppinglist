package shoppinglist.com.shoppinglist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import shoppinglist.com.shoppinglist.location.DummyLocationProvider;
import shoppinglist.com.shoppinglist.location.LocationProvider;
import shoppinglist.com.shoppinglist.database.ShoppingListDatabase;
import shoppinglist.com.shoppinglist.database.exceptions.PersistingFailedException;
import shoppinglist.com.shoppinglist.database.orm.ShoppingItem;

/**
 * Created by hsperr on 12/26/14.
 */
public class ShoppingListAdapter extends BaseAdapter{

    private final ShoppingListDatabase shoppingListDatabase;
    private final Context context;
    private List<ShoppingItem> items = null;
    private Comparator<? super ShoppingItem> compareItems;

    public ShoppingListAdapter(Context context, List<ShoppingItem> items, ShoppingListDatabase shoppingListDatabase) {
        this.context=context;
        this.items=items;
        this.shoppingListDatabase=shoppingListDatabase;
        this.compareItems=new ItemComparator();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row, parent, false);
        }

        final TextView itemNameText = (TextView)convertView.findViewById(R.id.itemName);
        final TextView itemPriceText = (TextView)convertView.findViewById(R.id.itemPrice);
        final CheckBox cb = (CheckBox)convertView.findViewById(R.id.checkItemOff);

        final ShoppingItem item = items.get(position);

        itemNameText.setText(item.getItemName());
        cb.setChecked(item.isBought());

        itemPriceText.setText("" + item.getPrice());
        if(cb.isChecked()){
            itemNameText.setTextColor(Color.GRAY);
        }else{
            itemNameText.setTextColor(Color.BLACK);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("CheckBoxShortClick", item + " cbChecked:" + cb.isChecked());
                toggleChecked(cb, item,  "0.0");
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   if(cb.isChecked()){
                       return true;
                   }

                   Log.i("CheckBoxLongClick", item + " cbChecked:" + cb.isChecked());

                   AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingListAdapter.this.context);
                   final EditText input = new EditText(ShoppingListAdapter.this.context);
                   input.setInputType(InputType.TYPE_CLASS_NUMBER);

                   builder.setTitle("Enter Price");
                   builder.setView(input);
                   builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           String price = input.getText().toString();
                           toggleChecked(cb, item, price);
                       }
                   });
                   builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.cancel();
                       }
                   });

                   AlertDialog dialog = builder.create();

                   dialog.getWindow().setSoftInputMode(
                           WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                   dialog.show();

                   return true;
               }
           }
        );

        return convertView;
    }

    //TODO pull is checked outside and unentangle all the dependencies
    private void toggleChecked(CheckBox cb, ShoppingItem item, String price) {
        cb.toggle();

        item.setBought(cb.isChecked());
        item.setPrice(Double.parseDouble(price));

        if(cb.isChecked()) {
            LocationProvider locationProvider = new DummyLocationProvider();
            Location location = locationProvider.getLocation();

            item.setLatitude(location.getLatitude());
            item.setLongitude(location.getLongitude());
            item.setTimestamp(new Date());
        }else{
            item.setLatitude(0.0);
            item.setLongitude(0.0);
            item.setTimestamp(null);
        }

        try {
            shoppingListDatabase.updateItem(item);
        } catch (PersistingFailedException e) {
            Toast.makeText(context, "Updating item failed.", Toast.LENGTH_SHORT).show();
        }

        Log.d("ItemToggle",item.toString());
        ShoppingListAdapter.this.notifyDataSetChanged();
    }


    public void addItem(ShoppingItem item) {
        this.items.add(item);
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.items.clear();
        this.notifyDataSetChanged();
    }
}
