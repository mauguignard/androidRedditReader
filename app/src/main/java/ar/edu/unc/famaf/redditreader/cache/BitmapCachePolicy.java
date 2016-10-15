package ar.edu.unc.famaf.redditreader.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mauguignard on 10/14/16.
 * Code based on: http://gabesechansoftware.com/disk-cacheing-on-android/
 */

class BitmapCachePolicy implements CachePolicy<Bitmap> {
    private Bitmap.CompressFormat mFormat;
    private int mQuality;

    BitmapCachePolicy() {
        mFormat = Bitmap.CompressFormat.JPEG;
        mQuality = 90;
    }

    /* Takes the quality and format to save to disk with.
     * Format is a constant in the Bitmap class, quality is 0-100.
     */
    public BitmapCachePolicy(Bitmap.CompressFormat format, int quality){
        mFormat = format;
        mQuality = quality;
    }

    @Override
    public boolean write(File outputFile, Bitmap value) throws IOException {
        FileOutputStream out = new FileOutputStream(outputFile);
        value.compress(mFormat, mQuality, out);

        out.close();

        return true;
    }

    @Override
    public Bitmap read(File inputFile) throws IOException {
        return BitmapFactory.decodeFile(inputFile.getPath());
    }

    @Override
    public long size(Bitmap value) {
        return value.getByteCount();
    }
}