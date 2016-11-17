package ar.edu.unc.famaf.redditreader.ui;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.backend.Backend;
import ar.edu.unc.famaf.redditreader.backend.EndlessScrollListener;
import ar.edu.unc.famaf.redditreader.backend.PostsIteratorListener;
import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsActivityFragment extends Fragment implements PostsIteratorListener {
    private static final int VISIBLE_THRESHOLD = 5;

    private OnPostItemSelectedListener mCallback;

    private PostAdapter adapter;
    private ListView PostsLV;
    private LinearLayout progressBarFooter;

    public interface OnPostItemSelectedListener {
        void onPostItemPicked(PostModel post);
    }

    public NewsActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnPostItemSelectedListener) context;
        } catch(ClassCastException e) {
            Log.e("NewsActivityFragment", context.toString() +
                    " must implement OnPostItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        final Context mContext = this.getContext();

        adapter = new PostAdapter(this.getContext(), R.layout.post_row, Backend.getInstance().getLst());

        PostsLV = (ListView) view.findViewById(R.id.postsListView);

        if (Backend.getInstance().getLst().isEmpty())
            Backend.getInstance().getTopPosts(mContext, this);
        else
            PostsLV.setLayoutAnimation(null);

        // Add dummy footer to avoid crash before API 19
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            PostsLV.addFooterView(new View(this.getContext()));

        PostsLV.setAdapter(adapter);

        progressBarFooter = (LinearLayout) getActivity().getLayoutInflater().inflate(
                R.layout.progress_bar_footer, null, false);

        // Recover the current page from the previous application state
        int currentPage = Backend.getInstance().getCurrentPage();

        PostsLV.setOnScrollListener(new EndlessScrollListener(VISIBLE_THRESHOLD, currentPage) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                if (PostsLV.getFooterViewsCount() == 0)
                    PostsLV.addFooterView(progressBarFooter);
                Backend.getInstance().getNextPosts(page - 1, NewsActivityFragment.this);

                return true;
            }
        });

        PostsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mCallback.onPostItemPicked((PostModel) adapterView.getAdapter().getItem(position));
            }
        });

        return view;
    }

    /* Since the list related to the Adapter is in the Backend, there is no need to pass as
     * parameter the list of new elements to this function, so 'posts' will always be null.
     */
    public void nextPosts(List<PostModel> posts) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            Backend.getInstance().setCurrentPage(adapter.getCount() / VISIBLE_THRESHOLD);
        }

        if (PostsLV.getFooterViewsCount() > 0)
            PostsLV.removeFooterView(progressBarFooter);
    }
}
