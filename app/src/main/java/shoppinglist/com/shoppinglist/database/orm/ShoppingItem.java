package shoppinglist.com.shoppinglist.database.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by hsperr on 3/28/15.
 */
@DatabaseTable(tableName = "item_list")
public class ShoppingItem {
    @DatabaseField(generatedId=true)
    long listItemId;

    @DatabaseField(foreign=true, foreignAutoRefresh = true)
    ShoppingList model;

    @DatabaseField
    String itemName;

    @DatabaseField
    double latitude;

    @DatabaseField
    double longitude;

    @DatabaseField
    double price;

    @DatabaseField
    boolean bought;

    @DatabaseField
    Date timestamp;

    public ShoppingItem() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getListItemId() {
        return listItemId;
    }

    public void setListItemId(long listItemId) {
        this.listItemId = listItemId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public ShoppingList getModel() {
        return model;
    }

    public void setModel(ShoppingList model) {
        this.model = model;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Item:"+this.getItemName());
        builder.append(" - ");
        builder.append("bought:"+this.isBought());
        builder.append(" - ");
        builder.append("price:"+this.getPrice());
        builder.append(" - ");
        builder.append("Lat:"+this.getLatitude());
        builder.append(" - ");
        builder.append("Lon:"+this.getLongitude());
        builder.append(" - ");
        builder.append("Date:"+this.getTimestamp());
        return builder.toString();
    }

}
