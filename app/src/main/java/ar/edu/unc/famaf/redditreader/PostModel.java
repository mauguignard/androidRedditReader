package ar.edu.unc.famaf.redditreader;

/**
 * Created by mauguignard on 9/20/16.
 */
public class PostModel {
    private String mSubreddit;
    private String mDescription;
    private int mNoComments;
    private long mDate;
    private String mIconURL;

    public String getSubreddit() {
        return mSubreddit;
    }

    public void setSubreddit(String name) {
        this.mSubreddit = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public int getNoComments() {
        return mNoComments;
    }

    public void setNoComments(int mNoComments) {
        this.mNoComments = mNoComments;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        this.mDate = date;
    }

    public String getIconURL() {
        return mIconURL;
    }

    public void setIconURL(String mIconURL) {
        this.mIconURL = mIconURL;
    }
}
