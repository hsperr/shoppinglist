package shoppinglist.com.shoppinglist.database.orm;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;

public class SeperatorItem implements Item {

    private DateTimeFormatter fmt = DateTimeFormat.forPattern("Y-M-d");
    private final DateTime date;
    private BigDecimal price = BigDecimal.ZERO;
    private int displayPrice;

    public SeperatorItem(DateTime date) {
        this.date = date;
    }

    public DateTime createdAt() {
        return date;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public boolean isSeperator() {
        return true;
    }

    public String getDisplayDate() {
        return fmt.print(date);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDisplayPrice() {
        return price.toString()+" ¥";
    }
}
