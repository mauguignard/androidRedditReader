package ar.edu.unc.famaf.redditreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import ar.edu.unc.famaf.redditreader.backend.Backend;
import ar.edu.unc.famaf.redditreader.ui.PostAdapter;

public class NewsActivity extends AppCompatActivity {

    private static final int LOGIN_CODE = 2;
    public final static String EMAIL_MESSAGE = "famaf.unc.edu.ar.activitiesassignment.EMAIL_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Backend.getInstance().getTopPosts();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.flairNoPress,
                R.color.flairPress6,
                R.color.flairPress5,
                R.color.flairPress4,
                R.color.flairPress3,
                R.color.flairPress2,
                R.color.flairPress1);


        PostAdapter adapter = new PostAdapter(this, R.layout.post_row,
                Backend.getInstance().getLst()) {
            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        };

        Backend.getInstance().setAdapter(adapter);

        if (Backend.getInstance().getLst().isEmpty()) {
            Backend.getInstance().getTopPosts();
            swipeContainer.setRefreshing(true);
        }

        final ListView PostsLV = (ListView) findViewById(R.id.subredditListView);
        PostsLV.setAdapter(adapter);

        final LinearLayout progressBarFooter = (LinearLayout) getLayoutInflater().inflate(
                R.layout.progress_bar_footer, null, false);

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
                    if (Math.abs(totalItemCount - previousTotal) >= 5) {
                        loading = false;
                        previousTotal = totalItemCount;
                        PostsLV.removeFooterView(progressBarFooter);
                    }
                } else if (totalItemCount - visibleItemCount <= firstVisibleItem + THRESHOLD) {
                    Backend.getInstance().getNextTopPosts();
                    loading = true;
                    PostsLV.addFooterView(progressBarFooter);

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
                String login_message = String.format("User %1$s logged in",
                                                     data.getStringExtra(EMAIL_MESSAGE));
                Log.i("LOGIN_SUCCESSFUL", login_message);
            } else {
                Log.i("LOGIN_UNSUCCESSFUL", "No user logged yet");

            }
        }
    }
}
