package ar.edu.unc.famaf.redditreader.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mauguignard on 10/27/16.
 */

class RedditDBHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = "RedditDBHelper";

    private static final String DATABASE_NAME = "redder.db";

    static final String POST_TABLE = "post";

    static final String POST_TABLE_ID = "_id";
    static final String POST_TABLE_DOMAIN = "domain";
    static final String POST_TABLE_SUBREDDIT = "subreddit";
    static final String POST_TABLE_GILDED = "gilded";
    static final String POST_TABLE_AUTHOR = "author";
    static final String POST_TABLE_NAME = "name";
    static final String POST_TABLE_SCORE= "score";
    static final String POST_TABLE_OVER18 = "over18";
    static final String POST_TABLE_THUMBNAIL = "thumbnail";
    static final String POST_TABLE_THUMBNAIL_FILE = "thumbnail_file";
    static final String POST_TABLE_PERMALINK = "permalink";
    static final String POST_TABLE_CREATED = "created";
    static final String POST_TABLE_LINK_FLAIR_TEXT = "link_flair_text";
    static final String POST_TABLE_URL = "url";
    static final String POST_TABLE_TITLE = "title";
    static final String POST_TABLE_NO_COMMENTS = "no_comments";
    static final String POST_TABLE_DOWNS = "downs";
    static final String POST_TABLE_UPS = "ups";


    RedditDBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSentence =
                "CREATE TABLE " + POST_TABLE + " ("
                + POST_TABLE_ID + " integer primary key autoincrement,"
                + POST_TABLE_DOMAIN + " text not null,"
                + POST_TABLE_SUBREDDIT + " text not null,"
                + POST_TABLE_GILDED + " integer,"
                + POST_TABLE_AUTHOR + " text not null,"
                + POST_TABLE_NAME + " text not null,"
                + POST_TABLE_SCORE + " integer,"
                + POST_TABLE_OVER18 + " integer,"
                + POST_TABLE_THUMBNAIL + " text not null,"
                + POST_TABLE_PERMALINK + " text not null,"
                + POST_TABLE_CREATED + " integer,"
                + POST_TABLE_LINK_FLAIR_TEXT + " text,"
                + POST_TABLE_URL + " text not null,"
                + POST_TABLE_TITLE + " text not null,"
                + POST_TABLE_NO_COMMENTS + " integer,"
                + POST_TABLE_DOWNS + " integer,"
                + POST_TABLE_UPS + " integer,"
                + POST_TABLE_THUMBNAIL_FILE + " blob" + ")";
        db.execSQL(createSentence);

        Log.i(LOG_TAG, "Database created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + POST_TABLE);
        this.onCreate(db);

        Log.i(LOG_TAG, "Database updated");
    }
}
