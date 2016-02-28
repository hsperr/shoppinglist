package shoppinglist.com.shoppinglist.database.orm;

import android.util.Log;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DatabaseTable(tableName="shopping_list")
public class ShoppingList {
    @DatabaseField(generatedId=true)
    long listId;

    @DatabaseField
    String name;

    @DatabaseField
    Date created;

    @ForeignCollectionField
    private ForeignCollection<ShoppingItem> items;

    private List<ShoppingItem> cachedItems = null;

    public ShoppingList(String name) {
        this.name = name;
        this.created = new Date();
    }

    public ShoppingList() {

    }

    public List<ShoppingItem> getItems() {
        if (cachedItems == null) {
            ArrayList<ShoppingItem> itemList = new ArrayList<>();
            for(ShoppingItem item :items){
                itemList.add(item);
            }
            this.cachedItems = itemList;
        }
        return this.cachedItems;
    }

    public void setItems(List<ShoppingItem> items) {
        this.clear();
        for (ShoppingItem item: items) {
            this.add(item);
        }
    }

    public void add(ShoppingItem item) {
        cachedItems.add(item);
        items.add(item);
    }

    public void remove(ShoppingItem item) {
        if(items.remove(item)) {
            cachedItems.remove(item);
        }
    }

    public void clear() {
        this.items.clear();
        this.cachedItems.clear();
    }

    public String getName() {
        return name;
    }

    public void setListName(String list_name) {
        this.name = list_name;
    }

    public Date getCreated() {
        return created;
    }

    public long getListId() {
        return listId;
    }

    public ShoppingItem get(int position) {
        return this.cachedItems.get(position);
    }

    public int size() {
        return this.getItems().size();
    }

    public BigDecimal getPrice() {
        BigDecimal price = BigDecimal.ZERO;
        for (ShoppingItem item: this.getItems()) {
            Log.i("Prices", this.getName()+""+item.getPrice());
            price = price.add(item.getPrice());
        }
        return price;
    }
}
