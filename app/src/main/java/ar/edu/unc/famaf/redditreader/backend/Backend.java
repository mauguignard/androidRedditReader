package ar.edu.unc.famaf.redditreader.backend;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.ui.PostAdapter;
import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by mauguignard on 9/29/16.
 */
public class Backend {
    private static final Backend ourInstance = new Backend();

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
        new GetTopPostsTask() {
            @Override
            void onError() {
                /* Trigger swipeContainer.setRefreshing(false); */
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }

            @Override
            void onSuccess(Listing result) {
                mLstPostsModel.clear();
                mLstPostsModel.addAll(result.getLstPostsModel());
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }
        };
    }

    public void getNextTopPosts() {
        String after = null;

        if (!mLstPostsModel.isEmpty())
            after = mLstPostsModel.get(mLstPostsModel.size() - 1).getName();

        new GetTopPostsTask(LIMIT, after) {
            @Override
            void onError() {
                /* Trigger swipeContainer.setRefreshing(false); */
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }

            @Override
            void onSuccess(Listing result) {
                mLstPostsModel.addAll(result.getLstPostsModel());
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }
        };
    }

}
