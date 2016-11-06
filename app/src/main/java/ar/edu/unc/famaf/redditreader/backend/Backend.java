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

    private Backend() {
        mLstPostsModel = new ArrayList<>();
    }

    public List<PostModel> getLst() {
        return mLstPostsModel;
    }

    public void getTopPosts(final Context context, final PostsIteratorListener listener) {
        new GetTopPostsTask(LIMIT) {
            @Override
            void onError() {
                Listing result = RedditDBHelper.getInstance().getPostsFromDB(0, LIMIT);

                if (result.getLstPostsModel().size() != 0) {
                    mLstPostsModel.clear();
                    mLstPostsModel.addAll(result.getLstPostsModel());
                } else {
                    Toast toast = Toast.makeText(context,
                            context.getString(R.string.no_internet_connection), Toast.LENGTH_LONG);
                    toast.show();
                }

                listener.nextPosts();
            }

            @Override
            void onSuccess(Listing result) {
                RedditDBHelper.getInstance().savePostsToDB(result, LIMIT);
                mLstPostsModel.clear();
                mLstPostsModel.addAll(result.getLstPostsModel());
                listener.nextPosts();
            }
        };
    }
}
