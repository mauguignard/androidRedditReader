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
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Locale;

import ar.edu.unc.famaf.redditreader.cache.BitmapCache;

/**
 * Created by mauguignard on 10/8/16.
 */

public class ImageLoader {
    final private Context mContext;

    final private ImageView mImageView;
    final private ProgressBar mProgressBar;

    // Radius in DP to apply in getRoundedCornerBitmap()
    final private int mRadiusInDP;

    public ImageLoader(@NonNull Context context, ImageView thumbnailIV, ProgressBar progressBar,
                       int radiusInDP) {
        this.mContext = context;
        this.mImageView = thumbnailIV;
        this.mProgressBar = progressBar;
        this.mRadiusInDP = radiusInDP;
    }

    public void load(String url) {
        PlaceHolderDrawable placeholder = null;

        Drawable drawable = mImageView.getDrawable();
        if (drawable instanceof PlaceHolderDrawable) {
            placeholder = (PlaceHolderDrawable) drawable;
            BitmapDownloadTask task = placeholder.getTask();
            if (task != null) {
                /* The ImageView has already an BitmapDownloadTask running, it is necessary
                to cancel it before starting a new one to avoid glitching due to row
                recycling of the adapter */
                task.softCancel();
                mProgressBar.setVisibility(View.GONE);
            }
        }

        Bitmap result = BitmapCache.getInstance().getBitmapFromMemCache(url);
        if (result != null) {
            mImageView.setImageBitmap(result);
        } else {
            BitmapDownloadTask downloader = new BitmapDownloadTask();

            if (placeholder != null) {
                placeholder.setTask(downloader);
            } else {
                mImageView.setImageDrawable(new PlaceHolderDrawable(downloader));
            }

            mProgressBar.setVisibility(View.VISIBLE);

            downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        }
    }

    private class BitmapDownloadTask extends AsyncTask<String, Void, Bitmap> {
        private static final String LOG_TAG = "BitmapDownloadTask";

        // WeakReference to prevent crash when ImageView and ProgressBar are for some reason gone
        private WeakReference<ImageView> mImageViewReference;
        private WeakReference<ProgressBar> mProgressBarReference;

        // Width and height of the ImageView
        private int mWidth, mHeight;

        private boolean mCancelled = false;

        BitmapDownloadTask() {
            this.mImageViewReference = new WeakReference<>(mImageView);
            this.mProgressBarReference = new WeakReference<>(mProgressBar);

            mImageView.post(new Runnable() {
                @Override
                public void run() {
                    mWidth = mImageView.getMeasuredWidth();
                    mHeight = mImageView.getMeasuredHeight();
                }
            });
        }

        void softCancel() {
            mCancelled = true;
        }

        protected Bitmap doInBackground(String ... urls) {
            String url = urls[0];
            InputStream is = null;

            Bitmap result = BitmapCache.getInstance().getBitmapFromDiskCache(url);
            if (result == null) {
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

                            if (bmp == null) {
                                if (isCancelled() || mCancelled) {
                                    return null;
                                } else {
                                    throw new UnknownError(
                                            "There was an error when decoding the stream");
                                }
                            }

                            // Resize Bitmap to ImageView size to reduce Memory usage
                            result = getResizedBitmap(bmp);

                            // Save Bitmap to Disk Cache
                            BitmapCache.getInstance().addBitmapToDiskCache(url, result);

                            // Apply some fancy rounded corners to Bitmap
                            result = getRoundedCornerBitmap(result);
                            break;
                    }
                } catch (Exception e) {
                    String msg = (e.getMessage() == null) ? "Unexpected error!" : e.getMessage();
                    Log.e(LOG_TAG, msg);
                    Log.e(LOG_TAG, "URL = " + url);
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "Unexpected error when closing InputStream");
                        }
                    }
                }
            } else {
                // Apply some fancy rounded corners to Bitmap
                result = getRoundedCornerBitmap(result);
            }

            // Save Bitmap to Memory Cache
            BitmapCache.getInstance().addBitmapToMemoryCache(url, result);

            return result;
        }

        protected void onPostExecute(final Bitmap result) {
            if (isCancelled() || mCancelled)
                return;

            ProgressBar progressBar = mProgressBarReference.get();
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }

            if (result == null) {
                Log.e(LOG_TAG, "Image is null!");
            } else {
                ImageView imageView = mImageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(result);

                    Animation fadeInAnimation = AnimationUtils.loadAnimation(
                            mContext, android.R.anim.fade_in);
                    imageView.startAnimation(fadeInAnimation);
                }
            }
        }

        private Bitmap decodeVectorResource(int resourceName) {
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
    }

    private class PlaceHolderDrawable extends ColorDrawable {
        private BitmapDownloadTask mTask = null;

        PlaceHolderDrawable(BitmapDownloadTask task) {
            super(Color.TRANSPARENT);
            this.mTask = task;
        }

        BitmapDownloadTask getTask() {
            return mTask;
        }

        void setTask(BitmapDownloadTask task) {
            this.mTask = task;
        }
    }
}