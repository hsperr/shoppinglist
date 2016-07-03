package shoppinglist.com.shoppinglist;

import shoppinglist.com.shoppinglist.database.orm.ShoppingItem;

public class ItemComparator implements java.util.Comparator<ShoppingItem> {

    @Override
    public int compare(ShoppingItem lhs, ShoppingItem rhs) {
        //a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
        if(lhs.getBoughtAt() == null && rhs.getBoughtAt() == null){
            return -lhs.getName().compareTo(rhs.getName());
        } else if (lhs.getBoughtAt() == null) {
            return -1;

        } else if (rhs.getBoughtAt() == null) {
            return 1;
        }
        return -lhs.getBoughtAt().compareTo(rhs.getBoughtAt());
    }
}
