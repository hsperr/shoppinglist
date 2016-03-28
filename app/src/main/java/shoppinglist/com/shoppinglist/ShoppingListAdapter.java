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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shoppinglist.com.shoppinglist.database.SQLItemRepository;
import shoppinglist.com.shoppinglist.database.orm.Item;
import shoppinglist.com.shoppinglist.database.orm.SeperatorItem;
import shoppinglist.com.shoppinglist.location.DummyLocationProvider;
import shoppinglist.com.shoppinglist.database.ItemRepository;
import shoppinglist.com.shoppinglist.database.exceptions.PersistingFailedException;
import shoppinglist.com.shoppinglist.database.orm.ShoppingItem;
import shoppinglist.com.shoppinglist.utils.SwipeDetector;


public class ShoppingListAdapter extends BaseAdapter{

    enum ItemTypes {
      ITEM_TYPE_SEPERATOR,
      ITEM_TYPE_ENTRY
    }

    private final ItemRepository shoppingListDatabase;
    private final Context context;
    private List<Item> list = new ArrayList<>();

    //private SortedMap<DateTime, ShoppingItem> items = new TreeMap<>();

    private Map<String, SeperatorItem> seperators = new HashMap<>();
    private Map<String, List<ShoppingItem>> items = new HashMap<>();

    private Comparator<? super ShoppingItem> compareItems;

    public ShoppingListAdapter(Context context, ItemRepository shoppingListDatabase) {
        this.context = context;
        this.shoppingListDatabase = shoppingListDatabase;
        this.compareItems = new ItemComparator();

        this.refresh();

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }



    @Override
    public int getViewTypeCount() {
        return ItemTypes.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        boolean isSection = list.get(position).isSeperator();

        if (isSection) {
            return ItemTypes.ITEM_TYPE_SEPERATOR.ordinal();
        }
        else {
            return ItemTypes.ITEM_TYPE_ENTRY.ordinal();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        if(getItemViewType(position)==ItemTypes.ITEM_TYPE_SEPERATOR.ordinal()) {
            if (convertView == null || convertView.findViewById(R.id.list_item_section_text) == null) {
                convertView = inflater.inflate(R.layout.seperator, parent, false);
            }
            final TextView sectionText = (TextView) convertView.findViewById(R.id.list_item_section_text);

            final SeperatorItem item = (SeperatorItem)this.list.get(position);
            sectionText.setText(item.getDisplayString());

        } else{
            if (convertView == null || convertView.findViewById(R.id.itemName) == null) {
                convertView = inflater.inflate(R.layout.item_row, parent, false);
            }

            final TextView itemNameText = (TextView)convertView.findViewById(R.id.itemName);
            final TextView itemPriceText = (TextView)convertView.findViewById(R.id.itemPrice);
            final CheckBox cb = (CheckBox)convertView.findViewById(R.id.checkItemOff);

            final ShoppingItem item = (ShoppingItem)this.list.get(position);
            itemNameText.setText(item.getName());
            cb.setChecked(item.getBoughtAt() != null);

            itemPriceText.setText("" + item.getPrice());
            if(cb.isChecked()){
                itemNameText.setTextColor(Color.GRAY);
            }else{
                itemNameText.setTextColor(Color.BLACK);
            }

            convertView.setOnTouchListener(new SwipeDetector(convertView, this, item));

            convertView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.i("CheckBoxShortClick", item + " cbChecked:" + cb.isChecked());
                    //toggleChecked(cb, item,  "0.0");
                    //   return;
                    // }

                    //Log.i("CheckBoxLongClick", item + " cbChecked:" + cb.isChecked());

                    if (cb.isChecked()) {
                        toggleChecked(cb, item, "0");
                    } else {
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
                    }
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                                                   @Override
                                                   public boolean onLongClick(View v) {
                                                       return true;
                                                   }
                                               }
            );
        }

        return convertView;
    }

    //TODO pull is checked outside and unentangle all the dependencies
    private void toggleChecked(CheckBox cb, ShoppingItem item, String price) {
        cb.toggle();



        DateTimeFormatter fmt = DateTimeFormat.forPattern("Y-M-d");

        if(cb.isChecked()) {
            item.setBoughtAt(DateTime.now());
            SeperatorItem seperator = seperators.get(item.getBoughtAt().toString(fmt));

            BigDecimal newPrice = new BigDecimal(price);
            item.setPrice(newPrice);
            seperator.setPrice(seperator.getPrice().add(newPrice));

            shoppinglist.com.shoppinglist.location.LocationProvider locationProvider = new DummyLocationProvider();
            Location location = locationProvider.getLocation();

            item.setLatitude(location.getLatitude());
            item.setLongitude(location.getLongitude());
            item.setCreatedAt(DateTime.now());
        }else{
            BigDecimal oldPrice = item.getPrice();
            SeperatorItem seperator = seperators.get(item.getBoughtAt().toString(fmt));
            seperator.setPrice(seperator.getPrice().subtract(oldPrice));

            item.setBoughtAt(null);
            item.setLatitude(0.0);
            item.setLongitude(0.0);
            item.setCreatedAt(null);
        }

        try {
            Log.d("ItemUpdate", ""+item);
            shoppingListDatabase.updateItem(item);
        } catch (PersistingFailedException e) {
            Toast.makeText(context, "Updating item failed.", Toast.LENGTH_SHORT).show();
        }

        Log.d("ItemToggle", item.toString());
        ShoppingListAdapter.this.notifyDataSetChanged();
    }


    public void removeItem(ShoppingItem item) {
        try {
            SQLItemRepository.getInstance(context).removeItem(item);
        } catch (PersistingFailedException e) {
            e.printStackTrace();
        }
        this.list.remove(item);
        this.refresh();
    }

    public void refresh() {
        list.clear();
        try {
            List<ShoppingItem> databaseItems = shoppingListDatabase.getItems();
            DateTime lastSeperatorDate = DateTime.now().withTimeAtStartOfDay();
            SeperatorItem lastSeperator = new SeperatorItem(lastSeperatorDate);
            seperators.put(lastSeperator.getDisplayDate(), lastSeperator);

            list.add(lastSeperator);
            Collections.sort(databaseItems, compareItems);


            BigDecimal price = BigDecimal.ZERO;
            for(ShoppingItem item: databaseItems){
                if(lastSeperatorDate.isAfter(item.createdAt())) {
                    lastSeperator.setPrice(price);
                    price = BigDecimal.ZERO;

                    lastSeperatorDate = lastSeperatorDate.minusDays(1);
                    lastSeperator = new SeperatorItem(lastSeperatorDate);
                    seperators.put(lastSeperator.getDisplayDate(), lastSeperator);
                    list.add(lastSeperator);

                }
                list.add(item);
                price = price.add(item.getPrice());
            }

            lastSeperator.setPrice(price);
        } catch (PersistingFailedException e) {
            Toast.makeText(context, "Could not load database.", Toast.LENGTH_SHORT);
            Log.e(ShoppingListAdapter.class.getName(), "couldn't load items from database.", e);
            this.list = new ArrayList<>();
        }

        this.notifyDataSetChanged();
    }

}
