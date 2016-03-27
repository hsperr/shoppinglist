package shoppinglist.com.shoppinglist;

import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import shoppinglist.com.shoppinglist.camera.CameraActivity;
import shoppinglist.com.shoppinglist.database.SQLItemRepository;
import shoppinglist.com.shoppinglist.database.ItemRepository;
import shoppinglist.com.shoppinglist.database.exceptions.PersistingFailedException;
import shoppinglist.com.shoppinglist.network.NetBlaster;

public class ShoppingListActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    ItemRepository shoppingListDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        shoppingListDatabase = SQLItemRepository.getInstance(this);


        final ShoppingListAdapter shoppingListAdapter = new ShoppingListAdapter(this, shoppingListDatabase);
        final ListView shoppingListView = (ListView)findViewById(R.id.shopping_list);
        shoppingListView.setAdapter(shoppingListAdapter);

        Button addButton = (Button)findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView inputText = (TextView) findViewById(R.id.shopping_input);
                String newItemName = String.valueOf(inputText.getText());
                if (!newItemName.matches("")) {
                    inputText.setText("");
                    Log.i(this.getClass().getName(), "Creating Item:" + newItemName);
                    try {
                        ShoppingListActivity.this.shoppingListDatabase.createNewItem(newItemName);
                        shoppingListAdapter.refresh();
                    }
                    catch(PersistingFailedException e){
                        Toast.makeText(ShoppingListActivity.this, "Inserting new item failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//        this.mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//        this.mGoogleApiClient.connect();
//        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        Toast.makeText(ShoppingListActivity.this, ""+mLastLocation, Toast.LENGTH_LONG).show();
    }

    public void onSendRequestClick() {
        NetBlaster.getInstance(this).addToRequestQueue(new StringRequest(
                Request.Method.GET, "http://google.com",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VolleyResponse", response.substring(0, 500));
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyResponse", error.getMessage());
                    }
                }
        ));
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
        switch (id) {
            case R.id.action_settings:
                return true;

            case R.id.action_scan:
                startActivity(new Intent(this, CameraActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
