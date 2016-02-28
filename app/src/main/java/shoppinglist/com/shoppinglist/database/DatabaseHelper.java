package shoppinglist.com.shoppinglist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import shoppinglist.com.shoppinglist.database.exceptions.PersistingFailedException;
import shoppinglist.com.shoppinglist.database.orm.ShoppingItem;
import shoppinglist.com.shoppinglist.database.orm.ShoppingList;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper implements ShoppingListDatabase {

    public static final String DATABASE_NAME = "shoppinglist";
    private static final int DATABASE_VERSION = 1;
    private Dao<ShoppingList, Integer> shoppingListDao = null;
    private Dao<ShoppingItem, Integer> listItemDao = null;

    static private DatabaseHelper instance;

    public static DatabaseHelper getInstance(Context c){
        if(instance == null){
            instance = new DatabaseHelper(c);
        }
        return instance;
    }
    private DatabaseHelper(Context c){
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Dao<ShoppingItem, Integer> getShoppingItemDao(){
        if (listItemDao==null){
            try {
                listItemDao=getDao(ShoppingItem.class);
            } catch (SQLException e) {
                Log.e(DatabaseHelper.class.getName(), "Could not get DAO object.",e);
                throw new RuntimeException(e);
            }
        }
        return listItemDao;
    }

    public Dao<ShoppingList, Integer> getShoppingListDao(){
        if (shoppingListDao==null){
            try {
                shoppingListDao=getDao(ShoppingList.class);
            } catch (SQLException e) {
                Log.e(DatabaseHelper.class.getName(), "Could not get DAO object.",e);
                throw new RuntimeException(e);
            }
        }
        return shoppingListDao;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, ShoppingList.class);
            TableUtils.createTable(connectionSource, ShoppingItem.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//      try {
//          Log.i(DatabaseHelper.class.getName(), "onUpgrade");
//          TableUtils.dropTable(connectionSource, ShoppingListModel.class, true);
//          TableUtils.dropTable(connectionSource, Shop.class, true);
//          TableUtils.dropTable(connectionSource, Item.class, true);
//          TableUtils.dropTable(connectionSource, Price.class, true);
//          onCreate(db, connectionSource);
//      } catch (SQLException e) {
//          Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
//          throw new RuntimeException(e);
//      }
    }

    @Override
    public ShoppingList getInitalShoppingList() throws PersistingFailedException {
        try {
            Dao<ShoppingList, Integer> dao = this.getShoppingListDao();
            QueryBuilder<ShoppingList, Integer> builder = dao.queryBuilder();
            builder.where().eq("last_used", true);

            List<ShoppingList> list = dao.query(builder.prepare());
            assert list.size()<=1;

            if(list.size()==0){
                Log.w(this.getClass().getName(), "No Shopping list found, creating new one.");
                return this.createNewShoppingList("default");
            }else{
                return list.get(0);
            }
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "Error inserting in SQL database"+e.getMessage());
            throw new PersistingFailedException(e);
        }
    }

    @Override
    public ShoppingList createNewShoppingList(String name) throws PersistingFailedException {
        Dao<ShoppingList, Integer> dao = this.getShoppingListDao();
        Date creationDate = new Date();

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setListName(name);
        shoppingList.setLast_used(true);
        shoppingList.setCreation_date(creationDate);

        try {
            dao.assignEmptyForeignCollection(shoppingList, "items");
            dao.create(shoppingList);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "Error inserting in SQL database"+e.getMessage());
            throw new PersistingFailedException(e);
        }
        return shoppingList;
    }

    @Override
    public void updateItem(ShoppingItem item) throws PersistingFailedException {
        Dao<ShoppingItem,Integer> dao = this.getShoppingItemDao();
        try {
            dao.update(item);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "Error updating SQL database"+e.getMessage());
            throw new PersistingFailedException(e);
        }
    }

    @Override
    public void removeItem(ShoppingItem item) throws PersistingFailedException {
         Dao<ShoppingItem,Integer> dao = this.getShoppingItemDao();
        try {
            dao.delete(item);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "Error deleting from SQL database"+e.getMessage());
            throw new PersistingFailedException(e);
        }
    }

    @Override
    public void clearList(ShoppingList shoppingList) throws PersistingFailedException {
          Dao<ShoppingItem,Integer> dao = this.getShoppingItemDao();
        try {
            dao.delete(shoppingList.getItems());
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "Error deleting from SQL database"+e.getMessage());
            throw new PersistingFailedException(e);
        }
    }

    @Override
    public ShoppingItem createNewItem(ShoppingList shoppingList, String newItemName) throws PersistingFailedException {
        ShoppingItem item = new ShoppingItem();
        item.setBought(false);
        item.setItemName(newItemName);
        item.setModel(shoppingList);
        try {
            this.getShoppingItemDao().create(item);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "Error inserting in SQL database"+e.getMessage());
            throw new PersistingFailedException(e);
        }
        return item;
    }
}
