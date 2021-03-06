package ar.edu.unc.famaf.redditreader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.cache.BitmapCache;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class NewsActivity extends AppCompatActivity
        implements  NewsActivityFragment.OnPostItemSelectedListener {

    private static final int LOGIN_CODE = 2;
    public static final String EMAIL_MESSAGE = "famaf.unc.edu.ar.activitiesassignment.EMAIL_MESSAGE";
    public static final String POST_EXTRA = "post_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Bitmap Disk Cache
        BitmapCache.getInstance().initDiskCache(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sign_in) {
            Intent login = new Intent(NewsActivity.this, LoginActivity.class);
            startActivityForResult(login, LOGIN_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Call Back method to get the Mail from LoginActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_CODE) {
            if (resultCode == RESULT_OK) {
                String login_message = String.format("User %1$s logged in",
                                                     data.getStringExtra(EMAIL_MESSAGE));
                Log.i("LOGIN_SUCCESSFUL", login_message);
            } else {
                Log.i("LOGIN_UNSUCCESSFUL", "No user logged yet");
            }
        }
    }

    public void onPostItemPicked(PostModel post) {
        Log.i("SELECTED ITEM", post.getTitle());

        Intent i = new Intent(NewsActivity.this, NewsDetailActivity.class);
        i.putExtra(POST_EXTRA, post);
        startActivity(i);
    }
}
