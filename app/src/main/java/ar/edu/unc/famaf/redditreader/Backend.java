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
                    mLstPostsModel.clear();
                    for (int i=0; i < children.length(); i++) {
                        JSONObject child = children.getJSONObject(i);
                        JSONObject childData = child.getJSONObject("data");
                        Log.i("JSON", childData.toString());

                        PostModel item = new PostModel();
                        item.setDomain(childData.getString("domain"));
                        item.setSubreddit("r/" + childData.getString("subreddit"));
                        item.setAuthor(childData.getString("author"));
                        item.setName(childData.getString("name"));
                        item.setScore(childData.getInt("score"));
                        item.setOver18(childData.getBoolean("over_18"));
                        item.setThumbnail(childData.getString("thumbnail"));
                        item.setPermalink(childData.getString("permalink"));
                        item.setCreated(childData.getLong("created_utc") * 1000);
                        item.setURL(childData.getString("url"));
                        item.setTitle(childData.getString("title"));
                        item.setNoComments(childData.getInt("num_comments"));
                        item.setDowns(childData.getInt("downs"));
                        item.setUps(childData.getInt("ups"));
                        mLstPostsModel.add(item);
                        Log.e("COUNT", Integer.toString(mLstPostsModel.size()));

                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                    }
                }catch (Exception e) {
                    Log.e("JSON_ERROR", "Unable to parse JSON object");
                    if (e.getMessage() != null) {
                        Log.e("JSON_ERROR", e.getMessage());
                    }
                    e.printStackTrace();
                }
            }
        };

        downloader.execute("https://www.reddit.com/top/.json");
    }
}
