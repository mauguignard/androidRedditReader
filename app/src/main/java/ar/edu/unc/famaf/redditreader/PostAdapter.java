package ar.edu.unc.famaf.redditreader;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mauguignard on 9/20/16.
 */
public class PostAdapter extends ArrayAdapter<PostModel> {
    private List<PostModel> mLstPostsModel = null;
    private Context context;

    public PostAdapter(Context context, int resource, List<PostModel> lst) {
        super(context, resource);
        this.mLstPostsModel = lst;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mLstPostsModel.size();
    }

    @Override
    public int getPosition(PostModel item) {
        return mLstPostsModel.indexOf(item);
    }

    @Override
    public PostModel getItem(int position) {
        return mLstPostsModel.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.post_row, parent, false);
        }

        final PostModel sm = mLstPostsModel.get(position);

        TextView subredditName = (TextView) convertView.findViewById(R.id.subredditName);
        TextView subredditDescription = (TextView) convertView.findViewById(R.id.subredditDescription);
        TextView subredditNoComments = (TextView) convertView.findViewById(R.id.subreditNoComments);
        TextView subredditDate = (TextView) convertView.findViewById(R.id.subredditLastUpdated);
        final ImageView subredditIcon = (ImageView) convertView.findViewById(R.id.subredditIcon);

        subredditName.setText(sm.getSubreddit());
        subredditDescription.setText(sm.getTitle());
        subredditNoComments.setText(String.format(context.getResources().getString(R.string.no_comments), sm.getNoComments()));
        final Picasso picasso = Picasso.with(context);
        picasso.setIndicatorsEnabled(true);
        picasso.load(sm.getThumbnail()).networkPolicy(NetworkPolicy.OFFLINE)
                .into(subredditIcon, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        picasso.load(sm.getThumbnail()).into(subredditIcon);
                    }
                });

        long time = sm.getCreated();
        long now = System.currentTimeMillis();
        CharSequence relativeTimeStr = DateUtils.getRelativeTimeSpanString(time,
                now, DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
        subredditDate.setText(relativeTimeStr);

        return convertView;
    }

    @Override
    public boolean isEmpty() {
        return mLstPostsModel.isEmpty();
    }
}
