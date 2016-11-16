package ar.edu.unc.famaf.redditreader.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.BuildConfig;
import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by mauguignard on 11/5/16.
 */
public class RedditDBHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = "RedditDBHelper";

    private static final String DATABASE_NAME = "redder.db";

    private static final String POST_TABLE = "post";

    private static final String POST_TABLE_ID = "_id";
    private static final String POST_TABLE_DOMAIN = "domain";
    private static final String POST_TABLE_SUBREDDIT = "subreddit";
    private static final String POST_TABLE_GILDED = "gilded";
    private static final String POST_TABLE_AUTHOR = "author";
    private static final String POST_TABLE_NAME = "name";
    private static final String POST_TABLE_SCORE = "score";
    private static final String POST_TABLE_OVER18 = "over18";
    private static final String POST_TABLE_THUMBNAIL = "thumbnail";
    private static final String POST_TABLE_THUMBNAIL_FILE = "thumbnail_file";
    private static final String POST_TABLE_PERMALINK = "permalink";
    private static final String POST_TABLE_CREATED = "created";
    private static final String POST_TABLE_LINK_FLAIR_TEXT = "link_flair_text";
    private static final String POST_TABLE_URL = "url";
    private static final String POST_TABLE_TITLE = "title";
    private static final String POST_TABLE_NO_COMMENTS = "no_comments";
    private static final String POST_TABLE_DOWNS = "downs";
    private static final String POST_TABLE_UPS = "ups";


    private static RedditDBHelper ourInstance = null;

    public static RedditDBHelper getInstance() {
        return ourInstance;
    }

    public static void init(Context context) {
        if (ourInstance == null)
            ourInstance = new RedditDBHelper(context.getApplicationContext());
    }

    private RedditDBHelper(Context context) {
        super(context, DATABASE_NAME, null, BuildConfig.VERSION_CODE);
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

        Log.i(LOG_TAG, "Database " + DATABASE_NAME + " created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + POST_TABLE);
        this.onCreate(db);

        Log.i(LOG_TAG, "Database updated");
    }

    Listing getPostsFromDB(int from, int limit) {
        SQLiteDatabase db = getReadableDatabase();

        List<PostModel> result = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + POST_TABLE, null);

        // Loop throw all rows until limit
        if (cursor.moveToPosition(from)) {
            int i = 0;
            while (!cursor.isAfterLast() && i < limit) {
                PostModel post = new PostModel();
                post.setDomain(cursor.getString(1));
                post.setSubreddit(cursor.getString(2));
                post.setGilded(cursor.getInt(3));
                post.setAuthor(cursor.getString(4));
                post.setName(cursor.getString(5));
                post.setScore(cursor.getInt(6));
                post.setOver18(cursor.getInt(7) != 0);
                post.setThumbnail(cursor.getString(8));
                post.setPermalink(cursor.getString(9));
                post.setCreated(cursor.getLong(10));
                post.setLinkFlairText(cursor.getString(11));
                post.setURL(cursor.getString(12));
                post.setTitle(cursor.getString(13));
                post.setNoComments(cursor.getInt(14));
                post.setDowns(cursor.getInt(15));
                post.setUps(cursor.getInt(16));
                result.add(post);
                cursor.moveToNext();
                i++;
            }
        }

        cursor.close();

        return new Listing(result, null, null);
    }

    void savePostsToDB(Listing listing, int limit) {
        SQLiteDatabase db = getWritableDatabase();

        // Use transaction to speed up multiple insertions
        db.beginTransaction();

        for (PostModel post : listing.getLstPostsModel()) {

            ContentValues values = new ContentValues();
            values.put(POST_TABLE_DOMAIN, post.getDomain());
            values.put(POST_TABLE_SUBREDDIT, post.getSubreddit());
            values.put(POST_TABLE_GILDED, post.getGilded());
            values.put(POST_TABLE_AUTHOR, post.getAuthor());
            values.put(POST_TABLE_NAME, post.getName());
            values.put(POST_TABLE_SCORE, post.getScore());
            values.put(POST_TABLE_OVER18, post.isOver18() ? 1 : 0);
            values.put(POST_TABLE_THUMBNAIL, post.getThumbnail());
            values.put(POST_TABLE_PERMALINK, post.getPermalink());
            values.put(POST_TABLE_CREATED, post.getCreated() / 1000);
            values.put(POST_TABLE_LINK_FLAIR_TEXT, post.getLinkFlairText());
            values.put(POST_TABLE_URL, post.getURL());
            values.put(POST_TABLE_TITLE, post.getTitle());
            values.put(POST_TABLE_NO_COMMENTS, post.getNoComments());
            values.put(POST_TABLE_DOWNS, post.getDowns());
            values.put(POST_TABLE_UPS, post.getUps());

            db.insert(POST_TABLE, null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        /* Remove old Posts (Limit max number of items in table) */
        String delQuery = "DELETE FROM " + POST_TABLE + " WHERE "
                + POST_TABLE_ID + " NOT IN ("
                + "SELECT " + POST_TABLE_ID + " FROM " + POST_TABLE
                + " ORDER BY " + POST_TABLE_ID + " DESC LIMIT " + limit + ");";

        db.rawQuery(delQuery, null);

    }

    public Bitmap getThumbnailFileFromDB(String key) {
        SQLiteDatabase db = getReadableDatabase();

        Bitmap result = null;

        Cursor cursor = db.rawQuery("SELECT " + POST_TABLE_THUMBNAIL_FILE +
                " FROM " + POST_TABLE + " WHERE " +
                POST_TABLE_THUMBNAIL + " = '" + key + "'", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            byte[] image =  cursor.getBlob(0);
            if (image != null)
                result = BitmapFactory.decodeByteArray(image, 0, image.length);
        }

        cursor.close();

        return result;
    }

    public void saveThumbnailFileToDB(String key, Bitmap bitmap) {
        SQLiteDatabase db = getWritableDatabase();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] byte_array = stream.toByteArray();

        ContentValues values = new ContentValues();
        values.put(POST_TABLE_THUMBNAIL_FILE, byte_array);

        db.update(POST_TABLE, values, POST_TABLE_THUMBNAIL + "= ?",
                new String[]{key});
    }
}
