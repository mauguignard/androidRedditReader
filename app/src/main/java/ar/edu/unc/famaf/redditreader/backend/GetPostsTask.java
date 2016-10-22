package ar.edu.unc.famaf.redditreader.backend;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ar.edu.unc.famaf.redditreader.model.Listing;

/**
 * Created by mauguignard on 10/22/16.
 */

abstract class GetPostsTask extends AsyncTask<String, Void, Listing> {
    private static final String LOG_TAG = "GetPostsTask";
    private static final String USER_AGENT =
            "android:ar.edu.unc.famaf.redditreader:v1.0";

    @Override
    protected Listing doInBackground(String ... urls) {
        InputStream is = null;
        Listing result = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urls[0]).openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("User-Agent:", USER_AGENT);

            connection.connect();

            // Expect HTTP 200 OK
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new Exception(String.format("Server returned HTTP %1$s: %2$s",
                        connection.getResponseCode(), connection.getResponseMessage()));
            }

            is = connection.getInputStream();
            if (is != null)
                result = Parser.readJsonStream(is);
        } catch (Exception e) {
            String msg = (e.getMessage() == null) ? "Unexpected error!" : e.getMessage();
            Log.e(LOG_TAG, msg);
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

        return result;
    }

    @Override
    protected void onPostExecute(Listing result) {
        if (result != null)
            onSuccess(result);
        else
            onError();
    }

    abstract void onError();
    abstract void onSuccess(Listing result);

}
