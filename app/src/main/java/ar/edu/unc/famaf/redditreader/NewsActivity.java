package ar.edu.unc.famaf.redditreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

public class NewsActivity extends AppCompatActivity {

    public final static int LOGIN_CODE = 2;
    public final static String EMAIL_MESSAGE = "famaf.unc.edu.ar.activitiesassignment.EMAIL_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PostAdapter adapter = new PostAdapter(this, R.layout.post_row,
                Backend.getInstance().getLst());
        Backend.getInstance().setAdapter(adapter);
        Backend.getInstance().getTopPosts();

        ListView PostsLV = (ListView) findViewById(R.id.subredditListView);
        PostsLV.setAdapter(adapter);

        PostsLV.setOnScrollListener(new AbsListView.OnScrollListener() {
            private static final int THRESHOLD = 5;
            private int previousTotal = 0;
            private boolean loading = true;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                } else if (totalItemCount - visibleItemCount <= firstVisibleItem + THRESHOLD) {
                    Backend.getInstance().getNextTopPosts();
                    loading = true;
                }
            }
        });
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
                String login_message = String.format(getString(R.string.event_user_logged_in),
                                                     data.getStringExtra(EMAIL_MESSAGE));
                Log.i("LOGIN_SUCCESSFUL", login_message);
            } else {
                Log.i("LOGIN_UNSUCCESSFUL", "No user logged yet");

            }
        }
    }
}
