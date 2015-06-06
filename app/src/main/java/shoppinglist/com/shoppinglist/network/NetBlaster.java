package shoppinglist.com.shoppinglist.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Volley request queue-er
 */
public class NetBlaster {

    private static NetBlaster instance;
    private static Context appContext;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    /*
     * Get instance of network queue
     */
    public static synchronized NetBlaster getInstance(Context context) {
        if (instance == null) {
            instance = new NetBlaster(context);
        }
        return instance;
    }

    private NetBlaster(Context context) {
        appContext = context.getApplicationContext();
        requestQueue = getRequestQueue(appContext);

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue(appContext).add(request);
    }
}
