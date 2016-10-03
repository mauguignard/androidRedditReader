package ar.edu.unc.famaf.redditreader;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mauguignard on 9/29/16.
 */
public class Backend {
    private static Backend ourInstance = new Backend();

    public static Backend getInstance() {
        return ourInstance;
    }

    private static final int LIMIT = 25;

    private final List<PostModel> mLstPostsModel;
    private PostAdapter adapter;

    private Backend() {
        mLstPostsModel = new ArrayList<>();
    }

    public List<PostModel> getLst() {
        return mLstPostsModel;
    }

    public void setAdapter(PostAdapter adapter) {
        this.adapter = adapter;
    }

    public void getTopPosts() {
        getTopPosts(false);
    }

    public void getNextTopPosts() {
        getTopPosts(true);
    }

    private void getTopPosts(final boolean append) {
        String after = null;

        if (append && !mLstPostsModel.isEmpty())
            after = mLstPostsModel.get(mLstPostsModel.size() - 1).getName();

        JSONDownloadTask downloader = new JSONDownloadTask() {
            @Override
            protected void onPostExecute(JSONObject result) {
                try {
                    JSONObject data = result.getJSONObject("data");
                    JSONArray children = data.getJSONArray("children");

                    if (!append)
                        mLstPostsModel.clear();

                    for (int i = 0; i < children.length(); i++) {
                        JSONObject child = children.getJSONObject(i);
                        JSONObject childData = child.getJSONObject("data");

                        PostModel item = new PostModel(childData);
                        mLstPostsModel.add(item);

                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                    }

                    Log.i("JSON", String.format("Read %1$d objects.", children.length()));
                }catch (Exception e) {
                    Log.e("JSON_ERROR", "Unable to parse JSON object");
                    if (e.getMessage() != null) {
                        Log.e("JSON_ERROR", e.getMessage());
                    }
                    e.printStackTrace();
                }
            }
        };

        String url = "https://www.reddit.com/top/.json?&limit=" + Integer.toString(LIMIT);

        if (after != null)
            url += "&after=" + after;

        downloader.execute(url);
    }

}
