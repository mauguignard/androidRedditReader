package ar.edu.unc.famaf.redditreader.model;

import java.util.List;

/**
 * Created by mauguignard on 10/22/16.
 */

public class Listing {
    private final List<PostModel> mLstPostsModel;
    private final String mAfter;
    private final String mBefore;

    public Listing(List<PostModel> lstPostsModel, String after, String before) {
        mLstPostsModel = lstPostsModel;
        mAfter = after;
        mBefore = before;
    }

    public List<PostModel> getLstPostsModel() {
        return mLstPostsModel;
    }

    public String getAfter() {
        return mAfter;
    }

    public String getBefore() {
        return mBefore;
    }
}
