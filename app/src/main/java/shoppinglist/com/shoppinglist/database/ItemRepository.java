package shoppinglist.com.shoppinglist.database;

import java.util.List;

import shoppinglist.com.shoppinglist.database.exceptions.PersistingFailedException;
import shoppinglist.com.shoppinglist.database.orm.ShoppingItem;

public interface ItemRepository {

    ShoppingItem createNewItem(String newItemName) throws PersistingFailedException;

    void updateItem(ShoppingItem item) throws PersistingFailedException;

    void removeItem(ShoppingItem item) throws PersistingFailedException;

    List<ShoppingItem> getItems() throws PersistingFailedException;
}
