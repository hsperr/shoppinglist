package shoppinglist.com.shoppinglist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import shoppinglist.com.shoppinglist.database.exceptions.PersistingFailedException;
import shoppinglist.com.shoppinglist.database.orm.ShoppingItem;

public class SQLItemRepository extends OrmLiteSqliteOpenHelper implements ItemRepository {

    public static final String DATABASE_NAME = "shoppinglist";
    private static final int DATABASE_VERSION = 3;

    private Dao<ShoppingItem, Integer> itemDao = null;

    static private SQLItemRepository instance;

    public static SQLItemRepository getInstance(Context c){
        if(instance == null){
            instance = new SQLItemRepository(c);
        }
        return instance;
    }

    private SQLItemRepository(Context c){
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Dao<ShoppingItem, Integer> getItemDao(){
        if (itemDao ==null){
            try {
                itemDao = getDao(ShoppingItem.class);
            } catch (SQLException e) {
                Log.e(SQLItemRepository.class.getName(), "Could not get DAO object.",e);
                throw new RuntimeException(e);
            }
        }
        return itemDao;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(SQLItemRepository.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, ShoppingItem.class);
        } catch (SQLException e) {
            Log.e(SQLItemRepository.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
      try {
          Log.i(SQLItemRepository.class.getName(), "onUpgrade");
          TableUtils.dropTable(connectionSource, ShoppingItem.class, true);
          onCreate(db, connectionSource);
      } catch (SQLException e) {
          Log.e(SQLItemRepository.class.getName(), "Can't drop databases", e);
          throw new RuntimeException(e);
      }
    }

    @Override
    public void updateItem(ShoppingItem item) throws PersistingFailedException {
        Dao<ShoppingItem,Integer> dao = this.getItemDao();
        try {
            dao.update(item);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "Error updating SQL database"+e.getMessage());
            throw new PersistingFailedException(e);
        }
    }

    @Override
    public void removeItem(ShoppingItem item) throws PersistingFailedException {
         Dao<ShoppingItem,Integer> dao = this.getItemDao();
        try {
            dao.delete(item);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "Error deleting from SQL database"+e.getMessage());
            throw new PersistingFailedException(e);
        }
    }

    @Override
    public List<ShoppingItem> getItems() throws PersistingFailedException {
        Dao<ShoppingItem, Integer> dao = this.getItemDao();
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "Error getting items from SQL database");
            throw new PersistingFailedException(e);
        }
    }

    @Override
    public ShoppingItem createNewItem(String newItemName) throws PersistingFailedException {
        ShoppingItem item = new ShoppingItem(newItemName);
        try {
            this.getItemDao().create(item);
        } catch (SQLException e) {
            Log.e(this.getClass().getName(), "Error inserting in SQL database"+e.getMessage());
            throw new PersistingFailedException(e);
        }
        return item;
    }
}
