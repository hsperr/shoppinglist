package shoppinglist.com.shoppinglist.location;

import android.location.Location;

public class DummyLocationProvider implements LocationProvider{

    private double latitude=32.00000;
    private double longitude=15.0000;

    public DummyLocationProvider(double lati, double longi){
        this.latitude=lati;
        this.longitude=longi;
    }

    public DummyLocationProvider() {

    }

    @Override
    public Location getLocation() {
        Location loc = new Location("");
        loc.setLatitude(this.latitude);
        loc.setLongitude(this.longitude);
        return loc;
    }
}
