package ar.edu.unc.famaf.redditreader.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;

public class NewsDetailActivityFragment extends Fragment {
    public NewsDetailActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);

        Intent intent = getActivity().getIntent();

        PostModel post = (PostModel) intent.getSerializableExtra(NewsActivity.POST_EXTRA);

        TextView subredditByAuthorTV = (TextView) view.findViewById(R.id.postSubredditByAuthor);
        TextView gildedTV = (TextView) view.findViewById(R.id.postGilded);
        TextView dateTV = (TextView) view.findViewById(R.id.postDate);
        TextView titleTV = (TextView) view.findViewById(R.id.postTitle);
        TextView bottomTV = (TextView) view.findViewById(R.id.postBottom);
        RelativeLayout previewRL = (RelativeLayout) view.findViewById(R.id.postPreview);
        final ImageView previewIV = (ImageView) view.findViewById(R.id.postPreviewIV);
        final ProgressBar previewPB = (ProgressBar) view.findViewById(R.id.postPreviewPB);


        String r = String.format(view.getContext().getResources().getString(R.string.subreddit_by_author),
                post.getSubreddit(), "#737373", post.getAuthor());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            subredditByAuthorTV.setText(Html.fromHtml(r,Html.FROM_HTML_MODE_LEGACY));
        } else {
            //noinspection deprecation
            subredditByAuthorTV.setText(Html.fromHtml(r));
        }

        if (post.getGilded() > 0)
            gildedTV.setText(String.format(Locale.US, "★%1$d", post.getGilded()));
        else
            gildedTV.setVisibility(View.GONE);

        long time = post.getCreated();
        long now = System.currentTimeMillis();
        CharSequence relativeTimeStr = DateUtils.getRelativeTimeSpanString(time,
                now, DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
        dateTV.setText(relativeTimeStr);

        titleTV.setText(post.getTitle());

        bottomTV.setText(String.format(view.getContext().getResources().getString(R.string.no_comments),
                post.getNoComments()));

        if (!post.getDomain().equals(String.format("self.%1$s", post.getSubreddit())))
            bottomTV.append(String.format(" • %1$s", post.getDomain()));

        if (post.getPreviewURL() != null)
            new AsyncTask<String, Void, Bitmap>() {
                @Override
                protected void onPreExecute() {
                    previewIV.setVisibility(View.GONE);
                    previewPB.setVisibility(View.VISIBLE);
                }

                @Override
                protected Bitmap doInBackground(String... urls) {
                    String url = urls[0];
                    Bitmap bmp = null;
                    try {
                        URL urln = new URL(url);
                        URLConnection con = urln.openConnection();
                        InputStream is = con.getInputStream();
                        bmp = BitmapFactory.decodeStream(is);
                        if (bmp != null)
                            return bmp;

                    } catch(Exception e) {
                        String msg = (e.getMessage() == null) ? "Unexpected error!" : e.getMessage();
                        Log.e("Error", msg);
                        e.printStackTrace();
                    }
                    return bmp;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    previewPB.setVisibility(View.GONE);

                    if (bitmap != null) {
                        previewIV.setVisibility(View.VISIBLE);
                        previewIV.setImageBitmap(bitmap);
                    }
                }
            }.execute(post.getPreviewURL());
        else
            previewRL.setVisibility(View.GONE);

        return view;
    }
}
