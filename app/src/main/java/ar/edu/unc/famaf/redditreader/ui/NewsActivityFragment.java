package ar.edu.unc.famaf.redditreader.ui;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.backend.Backend;
import ar.edu.unc.famaf.redditreader.backend.PostsIteratorListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsActivityFragment extends Fragment implements PostsIteratorListener {
    private SwipeRefreshLayout swipeContainer;
    private PostAdapter adapter;

    public NewsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        Backend.getInstance().init(this.getContext());

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Backend.getInstance().getTopPosts(NewsActivityFragment.this);
            }
        });

        swipeContainer.setColorSchemeResources(R.color.flairNoPress,
                R.color.flairPress6,
                R.color.flairPress5,
                R.color.flairPress4,
                R.color.flairPress3,
                R.color.flairPress2,
                R.color.flairPress1);

        adapter = new PostAdapter(this.getContext(), R.layout.post_row, Backend.getInstance().getLst());

        final ListView PostsLV = (ListView) view.findViewById(R.id.postsListView);

        if (Backend.getInstance().getLst().isEmpty()) {
            Backend.getInstance().getTopPosts(this);
            swipeContainer.setRefreshing(true);
        } else {
            PostsLV.setLayoutAnimation(null);
        }

        // Add dummy footer to avoid crash before API 19
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            PostsLV.addFooterView(new View(this.getContext()));
        }
        PostsLV.setAdapter(adapter);

        final LinearLayout progressBarFooter = (LinearLayout) getActivity().getLayoutInflater().
                inflate(R.layout.progress_bar_footer, null, false);

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
                    if (Math.abs(totalItemCount - previousTotal) >= THRESHOLD) {
                        loading = false;
                        previousTotal = totalItemCount;
                        PostsLV.removeFooterView(progressBarFooter);
                    }
                } else if (totalItemCount - visibleItemCount <= firstVisibleItem + THRESHOLD) {
                    Backend.getInstance().getNextTopPosts(NewsActivityFragment.this);
                    loading = true;
                    PostsLV.addFooterView(progressBarFooter);
                }
            }
        });

        return view;
    }

    public void nextPosts() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        if (swipeContainer != null) {
            swipeContainer.setRefreshing(false);
        }
    }
}
