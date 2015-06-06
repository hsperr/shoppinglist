package shoppinglist.com.shoppinglist.database;

import shoppinglist.com.shoppinglist.database.exceptions.PersistingFailedException;
import shoppinglist.com.shoppinglist.database.orm.ShoppingItem;
import shoppinglist.com.shoppinglist.database.orm.ShoppingList;

/**
 * Created by hsperr on 3/30/15.
 */
public interface ShoppingListDatabase {

    ShoppingList getInitalShoppingList() throws PersistingFailedException;

    ShoppingList createNewShoppingList(String name) throws PersistingFailedException;

    ShoppingItem createNewItem(ShoppingList shoppingList, String newItemName) throws PersistingFailedException;

    void updateItem(ShoppingItem item) throws PersistingFailedException;

    void removeItem(ShoppingItem item) throws PersistingFailedException;

    void clearList(ShoppingList shoppingList) throws PersistingFailedException;
}
