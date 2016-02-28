package shoppinglist.com.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import shoppinglist.com.shoppinglist.database.orm.ShoppingList;

public class ShoppingListListAdapter extends BaseAdapter {

    private final List<ShoppingList> list;
    private final Context context;

    public ShoppingListListAdapter(Context context, List<ShoppingList> list) {
        this.context = context;
        this.list = list;
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
        return list.get(position).getListId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.shoppinglistlist, parent, false);
        }

        final TextView listName = (TextView) convertView.findViewById(R.id.listName);
        final TextView listPrice = (TextView) convertView.findViewById(R.id.listPrice);

        final ShoppingList list = this.list.get(position);

        listName.setText(list.getName());
        listPrice.setText(list.getPrice().toString());

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("ListShortClick", list.toString());

                Intent myIntent = new Intent(context, ShoppingListActivity.class);
                myIntent.putExtra("listid", list.getListId());
                context.startActivity(myIntent);
            }
        });

        return convertView;
    }

    public void add(ShoppingList list) {
        Log.i("AddingList", list.getName()+" "+list.getItems().size()+" "+list.getCreated());
        this.list.add(list);
        this.notifyDataSetChanged();
    }
}
