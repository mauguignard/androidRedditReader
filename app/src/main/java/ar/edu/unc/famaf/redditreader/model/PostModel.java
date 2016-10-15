package ar.edu.unc.famaf.redditreader.model;

/**
 * Created by mauguignard on 9/20/16.
 */
public class PostModel {
    private String mDomain;
    private String mSubreddit;
    private int mGilded;
    private String mAuthor;
    private String mThumbnail;
    private long mCreated;
    private String mTitle;
    private int mNoComments;

    public String getDomain() {
        return mDomain;
    }

    public void setDomain(String domain) {
        mDomain = domain;
    }

    public String getSubreddit() {
        return mSubreddit;
    }

    public void setSubreddit(String subreddit) {
        mSubreddit = subreddit;
    }

    public int getGilded() {
        return mGilded;
    }

    public void setGilded(int gilded) {
        mGilded = gilded;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }

    public long getCreated() {
        return mCreated;
    }

    public void setCreated(long created) {
        mCreated = created * 1000;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getNoComments() {
        return mNoComments;
    }

    public void setNoComments(int noComments) {
        mNoComments = noComments;
    }
}