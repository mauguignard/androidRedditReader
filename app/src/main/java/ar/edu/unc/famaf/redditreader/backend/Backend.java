package ar.edu.unc.famaf.redditreader.backend;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.Listing;
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

    private Context mContext;

    private Backend() {
        mLstPostsModel = new ArrayList<>();
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public List<PostModel> getLst() {
        return mLstPostsModel;
    }

    public void getTopPosts(final PostsIteratorListener listener) {
        new GetTopPostsTask() {
            @Override
            void onError() {
                Toast toast = Toast.makeText(mContext,
                        mContext.getString(R.string.no_internet_connection), Toast.LENGTH_LONG);
                toast.show();
                listener.nextPosts();
            }

            @Override
            void onSuccess(Listing result) {
                mLstPostsModel.clear();
                mLstPostsModel.addAll(result.getLstPostsModel());
                listener.nextPosts();
            }
        };
    }

    public void getNextTopPosts(final PostsIteratorListener listener) {
        String after = null;

        if (!mLstPostsModel.isEmpty())
            after = mLstPostsModel.get(mLstPostsModel.size() - 1).getName();

        new GetTopPostsTask(LIMIT, after) {
            @Override
            void onError() {
                Toast toast = Toast.makeText(mContext,
                        mContext.getString(R.string.no_internet_connection), Toast.LENGTH_LONG);
                toast.show();
                listener.nextPosts();
            }

            @Override
            void onSuccess(Listing result) {
                mLstPostsModel.addAll(result.getLstPostsModel());
                listener.nextPosts();
            }
        };
    }
}
