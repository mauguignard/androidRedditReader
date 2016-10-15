package ar.edu.unc.famaf.redditreader.cache;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mauguignard on 10/14/16.
 * Code based on: http://gabesechansoftware.com/disk-cacheing-on-android/
 */

class DiskLruCache<ValueType> {
    private static final String LOG_TAG = "DiskLruCache";
    private static final String HASH_TYPE = "SHA-1";

    // Hold info about the cached data for fast lookup.
    private class Entry {
        String hash;
        File file;
        long byteCount;
        long modificationTime;

        Entry(String hash, File file, long byteCount, long modificationTime) {
            this.hash = hash;
            this.file = file;
            this.byteCount = byteCount;
            this.modificationTime = modificationTime;
        }
    }

    private File mDirectory;
    private CachePolicy<ValueType> mCachePolicy;

    private long mCurrentSize;
    private long mMaxSize;
    private long mMaxTime;

    private LinkedHashMap<String, Entry> mLruEntries;

    DiskLruCache(long maxSize, File directory, CachePolicy<ValueType> cachePolicy, long maxTime) {
        mMaxSize = maxSize;
        mDirectory = directory;
        mCachePolicy = cachePolicy;
        mMaxTime = maxTime;

        initializeFromDisk();
    }

    private boolean isFileStillValid(long modificationTime) {
        return System.currentTimeMillis() - mMaxTime < modificationTime;
    }

    private String getHashFromKey(String key) {
        String hash = "EMPTY_HASH";
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_TYPE);
            md.update(key.getBytes("UTF-8"));
            hash = new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }

        return hash;
    }

    private void initializeFromDisk() {
        if (!mDirectory.exists() && !mDirectory.mkdirs()) {
            Log.e(LOG_TAG, "Unable to create directory: " + mDirectory);
        }

        mCurrentSize = 0;
        mLruEntries = new LinkedHashMap<String, Entry>(0, 0.75f, true);

        File files[] = mDirectory.listFiles();
        List<Entry> cacheFiles = new ArrayList<>();

        /* Store all the files in a list, then sort them on reverse modification time.
         * The idea is that an older file still in the cache either is so tiny
         * it doesn't matter or is used a lot.
         */
        for (File file : files) {
            if (isFileStillValid(file.lastModified())) {
                Entry entry = new Entry(file.getName(), file, file.length(), file.lastModified());
                cacheFiles.add(entry);
            }
        }

        Collections.sort(cacheFiles, new Comparator<Entry>() {
            @Override
            public int compare(Entry entry1, Entry entry2) {
                long diff = entry1.modificationTime - entry2.modificationTime;
                if (diff > 0)
                    return 1;
                else if (diff < 0)
                    return -1;
                return 0;
            }
        });

        for (Entry entry : cacheFiles)
            addEntryToCache(entry);
    }

    private void removeFromCache(Entry entry) {
        if (!entry.file.delete()) {
            Log.e(LOG_TAG, "Unable to delete " + entry.file.getName());
        }

        mLruEntries.remove(entry.hash);
        mCurrentSize -= entry.byteCount;
    }

    synchronized ValueType get(String key) {
        Entry cachedEntry = mLruEntries.get(getHashFromKey(key));
        if (cachedEntry != null) {
            if (isFileStillValid(cachedEntry.modificationTime)) {
                try {
                    return mCachePolicy.read(cachedEntry.file);
                } catch (IOException e) {
                    // If we can't read the file, lets pretend it wasn't there
                    return null;
                }
            } else {
                // Not valid. Kick it from cache
                removeFromCache(cachedEntry);
            }
        }

        return null;
    }

    synchronized boolean put(String key, ValueType data) {
        long size = mCachePolicy.size(data);
        String hash = getHashFromKey(key);

        // If the object is already cached, remove it
        Entry cachedEntry = mLruEntries.get(hash);
        if (cachedEntry != null)
            removeFromCache(cachedEntry);

        if (!ensureFileCanFit(size))
            return false;

        // Perform the actual add
        File output = new File(mDirectory, hash);
        try {
            // Try to write the file.
            mCachePolicy.write(output, data);
        } catch (IOException e) {
            return false;
        }

        cachedEntry = new Entry(hash, output, size, System.currentTimeMillis());
        addEntryToCache(cachedEntry);

        return true;
    }

    private boolean ensureFileCanFit(long newItemSize) {
        // If it can't ever fit, short circuit
        if (newItemSize > mMaxSize)
            return false;

        // Remove enough objects until it fits
        while (mCurrentSize + newItemSize > mMaxSize && mLruEntries.size() > 0) {
            // Keep removing the head until we have room.
            Iterator<Entry> it = mLruEntries.values().iterator();
            Entry entry = it.next();
            removeFromCache(entry);
        }

        return true;
    }

    private void addEntryToCache(Entry entry) {
        mLruEntries.put(entry.hash, entry);
        mCurrentSize += entry.byteCount;
    }

    synchronized void remove(String key) {
        Entry cachedEntry = mLruEntries.get(getHashFromKey(key));
        if (cachedEntry != null)
            removeFromCache(cachedEntry);
    }

    synchronized void clear() {
        for (Map.Entry<String, Entry> entry : mLruEntries.entrySet()) {
            Entry cachedEntry = entry.getValue();
            if (!cachedEntry.file.delete()) {
                Log.e(LOG_TAG, "Unable to delete " + cachedEntry.file.getName());
            }
        }
        mLruEntries.clear();
        mCurrentSize = 0;
    }
}