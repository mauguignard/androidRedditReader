package ar.edu.unc.famaf.redditreader;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mauguignard on 10/1/16.
 */
class JSONDownloadTask extends AsyncTask<String, Void, JSONObject> {
    private static String USER_AGENT =
            "android:ar.edu.unc.famaf.redditreader:v1.0";

    private int limit;
    private String after;
    private String before;


    public JSONDownloadTask(int limit) {
        this.limit = limit;
        this.after = null;
        this.before = null;
    }

    public JSONDownloadTask(int limit, String after, String before) {
        this.limit = limit;
        this.after = after;
        this.before = before;
    }

    protected JSONObject doInBackground(String ... urls) {
        JSONObject jsonObj = null;

        try {
            String url = urls[0] + "?&limit=" + Integer.toString(limit);

            if (after != null)
                url += "&after=" + after;

            if (before != null)
                url += "&before=" + before;

            URLConnection connection = new URL(url).openConnection();
            connection.addRequestProperty("User-Agent:", USER_AGENT);

            InputStream is = connection.getInputStream();

            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

            jsonObj = new JSONObject(sb.toString());
        }catch (Exception e) {
            String msg = (e.getMessage() == null) ? "Unexpected error!" : e.getMessage();
            Log.e("Error", msg);
            e.printStackTrace();
        }

        if (jsonObj == null) {
            Log.e("JSON_ERROR", "Error downloading JSON");
        }

        return jsonObj;
    }

    protected void onPostExecute(JSONObject result) {
        // Does nothing
    }
}
