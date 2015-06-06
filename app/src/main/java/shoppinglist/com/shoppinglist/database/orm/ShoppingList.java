package shoppinglist.com.shoppinglist.database.orm;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hsperr on 12/26/14.
 */
@DatabaseTable(tableName="shopping_list")
public class ShoppingList {
    @DatabaseField(generatedId=true)
    long listId;

    @DatabaseField
    String list_name;

    @DatabaseField
    Date creation_date;

    @DatabaseField
    boolean last_used;

    @ForeignCollectionField
    private ForeignCollection<ShoppingItem> items;

    public List<ShoppingItem> getItems() {
        ArrayList<ShoppingItem> itemList = new ArrayList<>();
        for(ShoppingItem item :items){
            itemList.add(item);
        }
        return itemList;
    }

    public void setItems(ForeignCollection<ShoppingItem> items) {
        this.items = items;
    }

    public String getList_name() {
        return list_name;
    }

    public void setListName(String list_name) {
        this.list_name = list_name;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public boolean isLast_used() {
        return last_used;
    }

    public void setLast_used(boolean last_used) {
        this.last_used = last_used;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

}
