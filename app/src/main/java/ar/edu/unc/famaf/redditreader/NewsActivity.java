package ar.edu.unc.famaf.redditreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class NewsActivity extends AppCompatActivity {

    public final static int LOGIN_CODE = 2;
    public final static String EMAIL_MESSAGE = "famaf.unc.edu.ar.activitiesassignment.EMAIL_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NewsActivityFragment newsfragment = (NewsActivityFragment)
                getSupportFragmentManager().findFragmentById(R.id.news_activity_fragment_id);

        TextView subredditName = (TextView) findViewById(R.id.subredditName);
        subredditName.setText("r/todayilearned");

        TextView subredditLastUpdated = (TextView) findViewById(R.id.subredditLastUpdated);
        subredditLastUpdated.setText(String.format(getString(R.string.hours_ago), 4));

        TextView subredditDescription = (TextView) findViewById(R.id.subredditDescription);
        subredditDescription.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec");

        TextView subreditNoComments = (TextView) findViewById(R.id.subreditNoComments);
        subreditNoComments.setText(String.format(getString(R.string.no_comments), 2112));
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

                NewsActivityFragment newsfragment = (NewsActivityFragment)
                        getSupportFragmentManager().findFragmentById(R.id.news_activity_fragment_id);
                TextView textView = (TextView) findViewById(R.id.loginStatusTextView);
                textView.setText(login_message);
            }
        }
    }
}
