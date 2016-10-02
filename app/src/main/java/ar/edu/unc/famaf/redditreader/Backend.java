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

    private static int LIMIT = 25;

    private List<PostModel> mLstPostsModel;
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
        JSONDownloadTask downloader = new JSONDownloadTask(LIMIT) {
            @Override
            protected void onPostExecute(JSONObject result) {
                try {
                    JSONObject data = result.getJSONObject("data");
                    JSONArray children = data.getJSONArray("children");

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

        if (mLstPostsModel.isEmpty())
            downloader.execute("https://www.reddit.com/top/.json");
    }

}
