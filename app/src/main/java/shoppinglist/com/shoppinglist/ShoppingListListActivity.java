package shoppinglist.com.shoppinglist;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import shoppinglist.com.shoppinglist.database.DatabaseHelper;
import shoppinglist.com.shoppinglist.database.ShoppingListDatabase;
import shoppinglist.com.shoppinglist.database.exceptions.PersistingFailedException;
import shoppinglist.com.shoppinglist.database.orm.ShoppingList;


public class ShoppingListListActivity extends ActionBarActivity {

    ShoppingListDatabase shoppingListDatabase = null;
    ListView shoppingListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_list);

        shoppingListDatabase = DatabaseHelper.getInstance(getApplicationContext());

        //this.deleteDatabase(DatabaseHelper.DATABASE_NAME);

        List<ShoppingList> lists = new ArrayList<>();
        try {
            lists = shoppingListDatabase.getLists();
        } catch (PersistingFailedException e) {
            Toast.makeText(this, "Couldn't get ShoppingList.", Toast.LENGTH_SHORT).show();
        }

        final ShoppingListListAdapter adapter = new ShoppingListListAdapter(this, lists);
        shoppingListView = (ListView)findViewById(R.id.ShoppingListList);
        shoppingListView.setAdapter(adapter);

        Button addButton = (Button)findViewById(R.id.AddList);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView inputText = (TextView) findViewById(R.id.ShoppingListName);
                String newListName = String.valueOf(inputText.getText());
                if (!newListName.matches("")) {
                    inputText.setText("");
                    Log.i(this.getClass().getName(), "Creating List:" + newListName);
                    try {
                        ShoppingList list = ShoppingListListActivity.this.shoppingListDatabase.createNewList(newListName);
                        adapter.add(list);
                    } catch (PersistingFailedException e) {
                        Toast.makeText(ShoppingListListActivity.this, "Inserting new item failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        shoppingListView.deferNotifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_list_list, menu);
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
