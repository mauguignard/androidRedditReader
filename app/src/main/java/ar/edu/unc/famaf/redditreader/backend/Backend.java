package ar.edu.unc.famaf.redditreader.backend;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

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
                showNoConnectionDialog();
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
                showNoConnectionDialog();
                listener.nextPosts();
            }

            @Override
            void onSuccess(Listing result) {
                mLstPostsModel.addAll(result.getLstPostsModel());
                listener.nextPosts();
            }
        };
    }

    private void showNoConnectionDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        // Set Title
        alertDialogBuilder.setTitle(mContext.getString(R.string.no_internet_connection));

        // Set Dialog message
        alertDialogBuilder
                .setMessage(mContext.getString(R.string.unable_to_conn_reddit))
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });

        // Create Alert Dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Show it
        alertDialog.show();
    }
}
