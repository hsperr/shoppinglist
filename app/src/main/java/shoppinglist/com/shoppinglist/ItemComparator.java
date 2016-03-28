package shoppinglist.com.shoppinglist;

import shoppinglist.com.shoppinglist.database.orm.ShoppingItem;

public class ItemComparator implements java.util.Comparator<ShoppingItem> {

    @Override
    public int compare(ShoppingItem lhs, ShoppingItem rhs) {
        //a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
        return -lhs.createdAt().compareTo(rhs.createdAt());
    }
}
