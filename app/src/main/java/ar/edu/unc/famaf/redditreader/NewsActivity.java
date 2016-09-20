package ar.edu.unc.famaf.redditreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {

    public final static int LOGIN_CODE = 2;
    public final static String EMAIL_MESSAGE = "famaf.unc.edu.ar.activitiesassignment.EMAIL_MESSAGE";

    private static String[] subredditNames = {
            "r/funny",
            "r/funny",
            "r/creepy",
            "r/gifts",
            "r/pics"
    };
    private static String[] subredditDescriptions = {
            "Fantastic Name.",
            "Conan on sexism",
            "I asked my sculptor friend to make me something creepy using my grandfather's glass eye. He did.",
            "Somebody's got a spring in their step",
            "I stood here for an hour, in Mountain Lion country, waiting for the galaxy to align with the road. It was totally worth it."
    };

    private static String[] subredditIcons = {
            "https://b.thumbs.redditmedia.com/g3SGuAZgUFRZffXusvn9W8mgAQzzz-zx25rpw6fYeWA.jpg",
            "https://a.thumbs.redditmedia.com/1xhcjetJ67YFk6Udip4PE1K5_k6goJrgV1IkkCiMGg0.jpg",
            "https://b.thumbs.redditmedia.com/Saurgod807X2u6NgheWeM0dzjZshXIm4Es1xHcah6bA.jpg",
            "https://b.thumbs.redditmedia.com/POb0KpNagq07j8M5ZMzl0zyJqUpRRZHUG_S6Mae8yJc.jpg",
            "https://b.thumbs.redditmedia.com/ns-cFGDY5srMZ616vG3eL5OmGMd_s7Yo1qKpaqJJ7GQ.jpg"
    };

    private static int[] subredditNoComments = {
            153,
            396,
            154,
            553,
            601
    };

    private static long[] subredditDates = {
            System.currentTimeMillis() - 46800000,
            System.currentTimeMillis() - 14400000,
            System.currentTimeMillis() - 10800000,
            System.currentTimeMillis() - 32400000,
            System.currentTimeMillis() - 14400000
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NewsActivityFragment newsfragment = (NewsActivityFragment)
                getSupportFragmentManager().findFragmentById(R.id.news_activity_fragment_id);

        ArrayList<subredditModel> subredditList = new ArrayList<subredditModel>();

        for (int i = 0; i < 5; i++) {
            subredditModel item = new subredditModel();
            item.setName(subredditNames[i]);
            item.setDescription(subredditDescriptions[i]);
            item.setNoComments(subredditNoComments[i]);
            item.setDate(subredditDates[i]);
            item.setIconSrc(subredditIcons[i]);
            subredditList.add(item);
        }

        subredditAdapter adapter =
                new subredditAdapter(this, R.layout.subreddit_list_item, subredditList);
        ListView subredditLV = (ListView) findViewById(R.id.subredditListView);
        subredditLV.setAdapter(adapter);
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
            }
        }
    }
}
