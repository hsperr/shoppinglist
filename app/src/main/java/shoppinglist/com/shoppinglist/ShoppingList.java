package shoppinglist.com.shoppinglist;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import shoppinglist.com.shoppinglist.data.DummyLoader;
import shoppinglist.com.shoppinglist.data.FileHandler;


public class ShoppingList extends ActionBarActivity {


    ArrayList<ShoppingModel> data = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        data = new FileHandler().readDataFile(this);
        final ShoppingListAdapter shoppingListAdapter = new ShoppingListAdapter(this, data);

        final ListView shoppingList = (ListView)findViewById(R.id.shopping_list);
        shoppingList.setAdapter(shoppingListAdapter);

        Button addButton = (Button)findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView inputText = (TextView)findViewById(R.id.shopping_input);
                String newItemName = String.valueOf(inputText.getText());
                if(!newItemName.matches("")) {
                    inputText.setText("");
                    data.add(new ShoppingModel(newItemName, false));
                    shoppingListAdapter.notifyDataSetChanged();
                }
            }
        });
        Button clearButton = (Button)findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView inputText = (TextView)findViewById(R.id.shopping_input);
                inputText.setText("");
                data.clear();
                shoppingListAdapter.notifyDataSetChanged();
            }
        });

        Button newButton = (Button)findViewById(R.id.new_button);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView inputText = (TextView)findViewById(R.id.shopping_input);
                inputText.setText("");
                new FileHandler().writeData(data, ShoppingList.this);
                data.clear();
                shoppingListAdapter.notifyDataSetChanged();
            }
        });

        shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.get(position).bought = !data.get(position).bought;
                for(ShoppingModel model: data){
                    Log.e("ShortClick", model.name+" "+model.bought);
                }
            }
        });

        shoppingList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                data.remove(position);
                for(ShoppingModel model: data){
                    Log.e("LongClick", model.name+" "+model.bought);
                }
                shoppingListAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
