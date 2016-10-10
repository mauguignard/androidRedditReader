package ar.edu.unc.famaf.redditreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import ar.edu.unc.famaf.redditreader.cache.BitmapCache;

/**
 * Created by mauguignard on 10/8/16.
 */

public class BitmapDownloadTask extends AsyncTask<String, Void, Bitmap> {
    private static final String LOG_TAG = "BitmapDownloadTask";

    private Context mContext;

    // WeakReference to prevent crash when ImageView is for some reason gone
    private WeakReference<ImageView> mImageViewReference;

    // Width and height of the ImageView
    private int mWidth, mHeight;

    // Radius in DP to apply in getRoundedCornerBitmap()
    private int mRadiusInDP = 4;

    // Boolean to check if the resource was already in cache or not
    private boolean mInCache = true;

    public BitmapDownloadTask(Context context, ImageView imageView,
                              int width, int height, int radiusInDP) {
        this.mContext = context;
        this.mImageViewReference = new WeakReference<>(imageView);
        this.mWidth = width;
        this.mHeight = height;
        this.mRadiusInDP = radiusInDP;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mImageViewReference != null) {
            ImageView imageView = mImageViewReference.get();
            if (imageView != null) {
                Drawable drawable = imageView.getDrawable();
                if (drawable instanceof PlaceHolderDrawable) {
                    PlaceHolderDrawable placeholder = (PlaceHolderDrawable) drawable;
                    BitmapDownloadTask task = placeholder.getTask();
                    if (task != null) {
                        /* The ImageView has already an BitmapDownloadTask running, it is necessary
                        to cancel it before starting a new one to avoid glitching due to row
                        recycling of the adapter */
                        task.cancel(true);
                    }

                    placeholder.setTask(this);
                } else {
                    imageView.setImageDrawable(new PlaceHolderDrawable(this));
                }
            }
        }
    }

    protected Bitmap doInBackground(String ... urls) {
        String url = urls[0];
        InputStream is = null;

        // Check if the Bitmap is already in Cache
        Bitmap result = BitmapCache.getInstance().getBitmapFromMemCache(url);
        if (result == null) {
            mInCache = false;
            try {
                switch (url) {
                    case "nsfw":
                        result = decodeVectorResource(R.drawable.ic_nsfw_icon);
                        break;
                    case "self":
                        result = decodeVectorResource(R.drawable.ic_self_icon);
                        break;
                    case "default":
                        result = decodeVectorResource(R.drawable.ic_link_icon);
                        break;
                    case "image":
                        result = decodeVectorResource(R.drawable.ic_image_icon);
                        break;
                    default:
                        is = new URL(url).openConnection().getInputStream();
                        Bitmap bmp = BitmapFactory.decodeStream(is);

                        // Resize Bitmap to ImageView size to reduce Memory usage
                        result = getResizedBitmap(bmp);

                        // Apply some fancy rounded corners to Bitmap
                        result = getRoundedCornerBitmap(result);
                        break;
                }

                // Save Bitmap to Cache
                BitmapCache.getInstance().addBitmapToMemoryCache(url, result);
            } catch (Exception e) {
                String msg = (e.getMessage() == null) ? "Unexpected error!" : e.getMessage();
                Log.e(LOG_TAG, msg);
                Log.e(LOG_TAG, "URL = " + url);
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch(IOException e) {
                        Log.e(LOG_TAG, "Unexpected error when closing InputStream");
                    }
                }
            }
        }
        return result;
    }

    protected void onPostExecute(final Bitmap result) {
        if (isCancelled())
            return;

        if (result == null) {
            Log.e(LOG_TAG, "Image is null!");
        } else if (mImageViewReference != null) {
            ImageView imageView = mImageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(result);

                // If the Bitmap was not in cache, apply fade in transition
                if (!mInCache) {
                    Animation fadeInAnimation = AnimationUtils.loadAnimation(
                            mContext, android.R.anim.fade_in);
                    imageView.startAnimation(fadeInAnimation);
                }
            }
        }
    }

    protected Bitmap decodeVectorResource(int resourceName) {
        Drawable drawable = ContextCompat.getDrawable(mContext, resourceName);

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private Bitmap getResizedBitmap(Bitmap bmp) {
        Bitmap result;
        float scale = Math.min(((float) mWidth / bmp.getWidth()),
                ((float) mHeight / bmp.getHeight()));

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        result = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

        bmp.recycle();
        return result;
    }

    private Bitmap getRoundedCornerBitmap(Bitmap bmp) {
        Bitmap result = Bitmap.createBitmap(
                bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        float radius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mRadiusInDP, mContext.getResources().getDisplayMetrics());

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bmp, rect, rect, paint);

        bmp.recycle();
        return result;
    }

    private class PlaceHolderDrawable extends ColorDrawable {
        private BitmapDownloadTask mTask = null;

        public PlaceHolderDrawable(BitmapDownloadTask task) {
            super(Color.TRANSPARENT);
            this.mTask = task;
        }

        public BitmapDownloadTask getTask() {
            return mTask;
        }

        public void setTask(BitmapDownloadTask task) {
            this.mTask = task;
        }
    }
}