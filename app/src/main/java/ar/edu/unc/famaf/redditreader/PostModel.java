package ar.edu.unc.famaf.redditreader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mauguignard on 9/20/16.
 */
public class PostModel {
    private String mDomain;
    private String mSubreddit;
    private int mGilded;
    private String mAuthor;
    private String mName;
    private int mScore;
    private boolean mOver18;
    private String mThumbnail;
    private String mPermalink;
    private long mCreated;
    private String mLinkFlairText;
    private String mURL;
    private String mTitle;
    private int mNoComments;
    private int mDowns;
    private int mUps;

    public PostModel(JSONObject jsonObj) throws JSONException {
        this.mDomain = jsonObj.getString("domain");
        this.mSubreddit = jsonObj.getString("subreddit");
        this.mGilded = jsonObj.getInt("gilded");
        this.mAuthor = jsonObj.getString("author");
        this.mName = jsonObj.getString("name");
        this.mScore = jsonObj.getInt("score");
        this.mOver18 = jsonObj.getBoolean("over_18");
        this.mThumbnail = jsonObj.getString("thumbnail");
        this.mPermalink = jsonObj.getString("permalink");
        this.mCreated = jsonObj.getLong("created_utc") * 1000;
        this.mURL = jsonObj.getString("url");
        this.mTitle = jsonObj.getString("title");
        this.mNoComments = jsonObj.getInt("num_comments");
        this.mDowns = jsonObj.getInt("downs");
        this.mUps= jsonObj.getInt("ups");
    }

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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public boolean isOver18() {
        return mOver18;
    }

    public void setOver18(boolean over18) {
        mOver18 = over18;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }

    public String getPermalink() {
        return mPermalink;
    }

    public void setPermalink(String permalink) {
        mPermalink = permalink;
    }

    public long getCreated() {
        return mCreated;
    }

    public void setCreated(long created) {
        mCreated = created;
    }

    public String getLinkFlairText() {
        return mLinkFlairText;
    }

    public void setLinkFlairText(String linkFlairText) {
        mLinkFlairText = linkFlairText;
    }

    public String getURL() {
        return mURL;
    }

    public void setURL(String URL) {
        mURL = URL;
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

    public int getDowns() {
        return mDowns;
    }

    public void setDowns(int downs) {
        mDowns = downs;
    }

    public int getUps() {
        return mUps;
    }

    public void setUps(int ups) {
        mUps = ups;
    }
}
