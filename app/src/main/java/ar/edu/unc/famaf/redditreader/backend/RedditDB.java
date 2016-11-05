package ar.edu.unc.famaf.redditreader.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.BuildConfig;
import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by mauguignard on 10/28/16.
 */

class RedditDB {
    static Listing getPostsFromDB(Context context, int from, int limit) {
        RedditDBHelper dbHelper = new RedditDBHelper(context, BuildConfig.VERSION_CODE);

        List<PostModel> result = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + RedditDBHelper.POST_TABLE, null);

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
        db.close();

        return new Listing(result, null, null);
    }

    static void savePostsToDB(Context context, Listing listing, int limit) {
        RedditDBHelper dbHelper = new RedditDBHelper(context, BuildConfig.VERSION_CODE);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Use transaction to speed up multiple insertions
        db.beginTransaction();

        for (PostModel post : listing.getLstPostsModel()) {

            ContentValues values = new ContentValues();
            values.put(RedditDBHelper.POST_TABLE_DOMAIN, post.getDomain());
            values.put(RedditDBHelper.POST_TABLE_SUBREDDIT, post.getSubreddit());
            values.put(RedditDBHelper.POST_TABLE_GILDED, post.getGilded());
            values.put(RedditDBHelper.POST_TABLE_AUTHOR, post.getAuthor());
            values.put(RedditDBHelper.POST_TABLE_NAME, post.getName());
            values.put(RedditDBHelper.POST_TABLE_SCORE, post.getScore());
            values.put(RedditDBHelper.POST_TABLE_OVER18, post.isOver18() ? 1 : 0);
            values.put(RedditDBHelper.POST_TABLE_THUMBNAIL, post.getThumbnail());
            values.put(RedditDBHelper.POST_TABLE_PERMALINK, post.getPermalink());
            values.put(RedditDBHelper.POST_TABLE_CREATED, post.getCreated());
            values.put(RedditDBHelper.POST_TABLE_LINK_FLAIR_TEXT, post.getLinkFlairText());
            values.put(RedditDBHelper.POST_TABLE_URL, post.getURL());
            values.put(RedditDBHelper.POST_TABLE_TITLE, post.getTitle());
            values.put(RedditDBHelper.POST_TABLE_NO_COMMENTS, post.getNoComments());
            values.put(RedditDBHelper.POST_TABLE_DOWNS, post.getDowns());
            values.put(RedditDBHelper.POST_TABLE_UPS, post.getUps());

            db.insert(RedditDBHelper.POST_TABLE, null, values);
        }

        /* Remove old Posts (Limit max number of items in table) */
        String delQuery = "DELETE FROM " + RedditDBHelper.POST_TABLE + " WHERE "
                + RedditDBHelper.POST_TABLE_ID + " NOT IN ("
                + "SELECT " + RedditDBHelper.POST_TABLE_ID + " FROM " + RedditDBHelper.POST_TABLE
                + " ORDER BY " + RedditDBHelper.POST_TABLE_ID + " DESC LIMIT " + limit + ");";

        db.rawQuery(delQuery, null);


        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
}
