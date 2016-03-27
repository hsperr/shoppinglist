package shoppinglist.com.shoppinglist.database.orm;

import org.joda.time.DateTime;

public interface Item {

    long getId();

    boolean isSeperator();

    DateTime createdAt();
}
