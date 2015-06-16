package shoppinglist.com.shoppinglist.database.exceptions;

import java.sql.SQLException;

/**
 * Created by hsperr on 4/4/15.
 */
public class PersistingFailedException extends Exception {
    public PersistingFailedException(SQLException e) {
        super(e);
    }
}
