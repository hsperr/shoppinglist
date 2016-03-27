package shoppinglist.com.shoppinglist.database.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

import java.math.BigDecimal;

@DatabaseTable(tableName = "items")
public class ShoppingItem implements Item {
    @DatabaseField(generatedId=true)
    long id;

   @DatabaseField
    String name;

    @DatabaseField
    double latitude;

    @DatabaseField
    double longitude;

    @DatabaseField
    BigDecimal price;

    @DatabaseField
    DateTime createdAt;

    @DatabaseField
    DateTime boughtAt;

    public ShoppingItem() {

    }

    public ShoppingItem(String name) {
        this.name = name;
        this.createdAt = DateTime.now();
        this.boughtAt = null;
        this.price = BigDecimal.ZERO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public BigDecimal getPrice() {
        if(this.price == null){
            return BigDecimal.ZERO;
        }
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public DateTime getBoughtAt() {
        return boughtAt;
    }

    public void setBoughtAt(DateTime boughtAt) {
        this.boughtAt = boughtAt;
    }

    public DateTime createdAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Item:"+this.getName());
        builder.append(" - ");
        builder.append("bought:"+this.getBoughtAt());
        builder.append(" - ");
        builder.append("price:"+this.getPrice());
        builder.append(" - ");
        builder.append("Lat:"+this.getLatitude());
        builder.append(" - ");
        builder.append("Lon:"+this.getLongitude());
        builder.append(" - ");
        builder.append("Date:"+this.createdAt());
        return builder.toString();
    }

    public long getId() {
        return this.id;
    }

    @Override
    public boolean isSeperator() {
        return false;
    }
}
