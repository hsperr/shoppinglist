package shoppinglist.com.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import shoppinglist.com.shoppinglist.Location.DummyLocationProvider;
import shoppinglist.com.shoppinglist.Location.LocationProvider;
import shoppinglist.com.shoppinglist.database.ShoppingListDatabase;
import shoppinglist.com.shoppinglist.database.exceptions.PersistingFailedException;
import shoppinglist.com.shoppinglist.database.orm.ShoppingItem;
import shoppinglist.com.shoppinglist.database.orm.ShoppingList;

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

        final TextView name = (TextView)convertView.findViewById(R.id.textView1);
        final CheckBox cb = (CheckBox)convertView.findViewById(R.id.checkBox1);

        name.setText(items.get(position).getItemName());
        cb.setChecked(items.get(position).isBought());

        if(cb.isChecked()){
            name.setTextColor(Color.GRAY);
        }else{
            name.setTextColor(Color.BLACK);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cb.toggle();

                Log.i("checkbox", items.get(position).getItemName() + " p " + position + " " + cb.isChecked());


                if(cb.isChecked()){
                    name.setTextColor(Color.GRAY);
                }else{
                    name.setTextColor(Color.BLACK);
                }
                try {
                    ShoppingItem item = items.get(position);
                    item.setBought(cb.isChecked());
                    LocationProvider locationProvider = new DummyLocationProvider();
                    Location location = locationProvider.getLocation();
                    item.setLatitude(location.getLatitude());
                    item.setLatitude(location.getLongitude());
                    item.setTimestamp(new Date());
                    shoppingListDatabase.updateItem(item);
                } catch (PersistingFailedException e) {
                    Toast.makeText(context, "Updating item failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   Log.i("delete", items.get(position).getItemName() + " p " + position + " " + cb.isChecked());
                   //TODO long click will eventually be price insertion, made it delete for now
                   ShoppingItem item = items.remove(position);
                   try {
                       shoppingListDatabase.removeItem(item);

                   } catch (PersistingFailedException e) {
                       Toast.makeText(context, "removing item failed.", Toast.LENGTH_SHORT).show();
                   }
                   ShoppingListAdapter.this.notifyDataSetChanged();
                   return true;
               }
           }
        );

        return convertView;
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
