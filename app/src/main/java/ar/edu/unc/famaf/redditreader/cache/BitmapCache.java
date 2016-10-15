package ar.edu.unc.famaf.redditreader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;

import java.io.File;

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

    private DiskLruCache<Bitmap> mDiskLruCache;
    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static final String DISK_CACHE_SUBDIR = "thumbnails";
    private static final long DISK_CACHE_MAX_TIME = 7 * 24 * 60 * 60 * 1000L; // One week

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
        // Initialize disk cache on background thread
        if (mDiskLruCache == null) {
            File cacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR);
            new InitDiskCacheTask().execute(cacheDir);
        }
    }

    private class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
        @Override
        protected Void doInBackground(File... params) {
            synchronized (mDiskCacheLock) {
                File cacheDir = params[0];
                mDiskLruCache = new DiskLruCache<>(
                        DISK_CACHE_SIZE, cacheDir, new BitmapCachePolicy(), DISK_CACHE_MAX_TIME);
                mDiskCacheStarting = false; // Finished initialization
                mDiskCacheLock.notifyAll(); // Wake any waiting threads
            }
            return null;
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public void addBitmapToDiskCache(String key, Bitmap bitmap) {
        if (mDiskLruCache != null && mDiskLruCache.get(key) == null) {
                mDiskLruCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public Bitmap getBitmapFromDiskCache(String key) {
        synchronized (mDiskCacheLock) {
            // Wait while disk cache is started from background thread
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {
                    Log.e("BitmapCache", e.getMessage());
                }
            }
            if (mDiskLruCache != null) {
                return mDiskLruCache.get(key);
            }
        }
        return null;
    }

    // Creates a unique subdirectory of the designated app cache directory.
    private static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath = context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }
}