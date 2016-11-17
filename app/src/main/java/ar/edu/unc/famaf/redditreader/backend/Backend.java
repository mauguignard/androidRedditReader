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

    private static final int LIMIT = 50;

    private final List<PostModel> mLstPostsModel;

    private int mCurrentPage;

    private Backend() {
        mLstPostsModel = new ArrayList<>();
        mCurrentPage = 0;
    }

    public List<PostModel> getLst() {
        return mLstPostsModel;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    public void getTopPosts(final Context context, final PostsIteratorListener listener) {
        new GetTopPostsTask(LIMIT) {
            @Override
            void onError() {
                // Just return the first 5 posts
                Listing result = RedditDBHelper.getInstance().getPostsFromDB(0, 5);

                if (result.getLstPostsModel().size() != 0) {
                    mLstPostsModel.clear();
                    mLstPostsModel.addAll(result.getLstPostsModel());
                } else {
                    Toast toast = Toast.makeText(context,
                            context.getString(R.string.no_internet_connection), Toast.LENGTH_LONG);
                    toast.show();
                }

                listener.nextPosts(null);
            }

            @Override
            void onSuccess(Listing result) {
                RedditDBHelper.getInstance().savePostsToDB(result, LIMIT);
                mLstPostsModel.clear();

                // Just return the first 5 posts
                mLstPostsModel.addAll(result.getLstPostsModel().subList(0, 5));
                listener.nextPosts(null);
            }
        };
    }

    public void getNextPosts(int page, final PostsIteratorListener listener) {
        // Get the next 5 posts starting at the element n°(page * 5) (if available)
        Listing result = RedditDBHelper.getInstance().getPostsFromDB(page * 5, 5);
        mLstPostsModel.addAll(result.getLstPostsModel());

        listener.nextPosts(null);
    }
}
