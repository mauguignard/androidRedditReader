package ar.edu.unc.famaf.redditreader.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by mauguignard on 10/8/16.
 * Code from: https://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
 */
public class BitmapCache {
    private static BitmapCache ourInstance = new BitmapCache();

    public static BitmapCache getInstance() {
        return ourInstance;
    }

    private LruCache<String, Bitmap> mMemoryCache;

    private BitmapCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}