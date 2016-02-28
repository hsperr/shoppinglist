package shoppinglist.com.shoppinglist;

import shoppinglist.com.shoppinglist.database.orm.ShoppingItem;
import shoppinglist.com.shoppinglist.database.orm.ShoppingList;

public class ItemComparator implements java.util.Comparator<ShoppingItem> {

    @Override
    public int compare(ShoppingItem lhs, ShoppingItem rhs) {
        //a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
        if(lhs.isBought() && !rhs.isBought()){
            return 1;
        }else if (rhs.isBought() && !lhs.isBought()){
            return -1;
        }

        return lhs.getItemName().compareTo(rhs.getItemName());
    }
}
