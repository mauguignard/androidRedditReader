package ar.edu.unc.famaf.redditreader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
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
class PostAdapter extends ArrayAdapter<PostModel> {
    private List<PostModel> mLstPostsModel = null;
    private Context context;

    static class ViewHolderItem {
        TextView subredditByAuthorTV;
        TextView gildedTV;
        TextView titleTV;
        TextView bottomTV;
        TextView dateTV;
        ImageView thumbnailIV;
    }

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

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final ViewHolderItem viewHolder;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.post_row, parent, false);

            viewHolder = new ViewHolderItem();
            viewHolder.subredditByAuthorTV = (TextView) convertView.findViewById(R.id.postSubredditByAuthor);
            viewHolder.gildedTV = (TextView) convertView.findViewById(R.id.postGilded);
            viewHolder.titleTV = (TextView) convertView.findViewById(R.id.postTitle);
            viewHolder.bottomTV = (TextView) convertView.findViewById(R.id.postBottom);
            viewHolder.dateTV = (TextView) convertView.findViewById(R.id.postDate);
            viewHolder.thumbnailIV = (ImageView) convertView.findViewById(R.id.postThumbnail);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        final PostModel sm = mLstPostsModel.get(position);

        String r = String.format(context.getResources().getString(R.string.subreddit_by_author),
                sm.getSubreddit(), "#737373", sm.getAuthor());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            viewHolder.subredditByAuthorTV.setText(Html.fromHtml(r,Html.FROM_HTML_MODE_LEGACY));
        } else {
            viewHolder.subredditByAuthorTV.setText(Html.fromHtml(r));
        }

        if (sm.getGilded() > 0) {
            viewHolder.gildedTV.setVisibility(View.VISIBLE);
            viewHolder.gildedTV.setText(String.format("★%1$d", sm.getGilded()));
        } else {
            viewHolder.gildedTV.setVisibility(View.GONE);
        }

        viewHolder.titleTV.setText(sm.getTitle());

        viewHolder.bottomTV.setText(String.format(context.getResources().getString(R.string.no_comments), sm.getNoComments()));

        if (!sm.getDomain().equals(String.format("self.%1$s", sm.getSubreddit())))
            viewHolder.bottomTV.append(String.format(" • %1$s", sm.getDomain()));

        if (sm.isOver18())
            viewHolder.thumbnailIV.setImageResource(R.drawable.ic_nsfw_icon);
        else if (sm.getThumbnail().equals("self"))
            viewHolder.thumbnailIV.setImageResource(R.drawable.ic_self_icon);
        else if (sm.getThumbnail().equals("default"))
            viewHolder.thumbnailIV.setImageResource(R.drawable.ic_link_icon);
        else {
            final Picasso picasso = Picasso.with(context);
            picasso.setIndicatorsEnabled(true);
            picasso.load(sm.getThumbnail()).networkPolicy(NetworkPolicy.OFFLINE)
                    .into(viewHolder.thumbnailIV, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            picasso.load(sm.getThumbnail()).into(viewHolder.thumbnailIV);
                        }
                    });
        }

        long time = sm.getCreated();
        long now = System.currentTimeMillis();
        CharSequence relativeTimeStr = DateUtils.getRelativeTimeSpanString(time,
                now, DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
        viewHolder.dateTV.setText(relativeTimeStr);

        return convertView;
    }

    @Override
    public boolean isEmpty() {
        return mLstPostsModel.isEmpty();
    }
}
