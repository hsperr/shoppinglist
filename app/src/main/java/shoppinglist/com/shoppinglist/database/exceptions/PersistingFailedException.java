package shoppinglist.com.shoppinglist.database.exceptions;

import java.sql.SQLException;

public class PersistingFailedException extends Exception {
    public PersistingFailedException(SQLException e) {
        super(e);
    }
}
