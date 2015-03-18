package shoppinglist.com.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import shoppinglist.com.shoppinglist.data.DataLoader;

/**
 * Created by hsperr on 12/26/14.
 */
public class ShoppingListAdapter extends BaseAdapter{

    ArrayList<ShoppingModel> shoppingData = null;
    Context context;

    public ShoppingListAdapter(ShoppingList con, ArrayList<ShoppingModel> data) {
        shoppingData = data;
        context = con;
    }

    @Override
    public int getCount() {
        return shoppingData.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingData.get(position);
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

        name.setText(shoppingData.get(position).name);
        cb.setChecked(shoppingData.get(position).bought);

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cb.toggle();
                Log.i("checkbox", shoppingData.get(position).name + " p " + position + " " + cb.isChecked());
                shoppingData.get(position).bought = cb.isChecked();
            }
        });


        return convertView;
    }
}
