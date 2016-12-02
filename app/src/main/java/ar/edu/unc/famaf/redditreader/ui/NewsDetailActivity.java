package ar.edu.unc.famaf.redditreader.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ar.edu.unc.famaf.redditreader.R;

public class NewsDetailActivity extends AppCompatActivity
        implements NewsDetailActivityFragment.OnURLButtonClickListener {
    public static final String POST_URL_EXTRA = "post_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
    }

    @Override
    public void onURLButtonClicked(String url) {
        Intent i = new Intent(NewsDetailActivity.this, WebBrowserActivity.class);
        i.putExtra(POST_URL_EXTRA, url);
        startActivity(i);
    }
}
