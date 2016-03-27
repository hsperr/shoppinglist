package shoppinglist.com.shoppinglist;

import net.danlew.android.joda.JodaTimeAndroid;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
