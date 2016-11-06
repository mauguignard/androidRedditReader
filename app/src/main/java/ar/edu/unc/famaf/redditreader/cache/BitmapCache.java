package ar.edu.unc.famaf.redditreader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import ar.edu.unc.famaf.redditreader.backend.RedditDBHelper;

/**
 * Created by mauguignard on 10/8/16.
 * Code from: https://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
 */
public class BitmapCache {
    private static final String LOG_TAG = "BitmapCache";
    private static final BitmapCache ourInstance = new BitmapCache();

    public static BitmapCache getInstance() {
        return ourInstance;
    }

    private final LruCache<String, Bitmap> mMemoryCache;

    private BitmapCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void initDiskCache(Context context) {
        RedditDBHelper.init(context);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public void addBitmapToDiskCache(String key, Bitmap bitmap) {
        RedditDBHelper.getInstance().saveThumbnailFileToDB(key, bitmap);
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public Bitmap getBitmapFromDiskCache(String key) {
        return RedditDBHelper.getInstance().getThumbnailFileFromDB(key);
    }
}