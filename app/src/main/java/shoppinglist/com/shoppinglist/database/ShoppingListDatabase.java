package shoppinglist.com.shoppinglist.database;

import java.util.List;

import shoppinglist.com.shoppinglist.database.exceptions.PersistingFailedException;
import shoppinglist.com.shoppinglist.database.orm.ShoppingItem;
import shoppinglist.com.shoppinglist.database.orm.ShoppingList;

public interface ShoppingListDatabase {

    List<ShoppingList> getLists() throws PersistingFailedException;

    ShoppingList getList(long id) throws PersistingFailedException;

    ShoppingList createNewList(String name) throws PersistingFailedException;

    ShoppingItem createNewItem(ShoppingList shoppingList, String newItemName) throws PersistingFailedException;

    void updateItem(ShoppingItem item) throws PersistingFailedException;

    void removeItem(ShoppingItem item) throws PersistingFailedException;

    void clearList(ShoppingList shoppingList) throws PersistingFailedException;
}
