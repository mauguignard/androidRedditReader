package ar.edu.unc.famaf.redditreader.backend;

import ar.edu.unc.famaf.redditreader.model.Listing;

/**
 * Created by mauguignard on 10/22/16.
 */

abstract class GetTopPostsTask extends GetPostsTask {
    private static final String TOP_POSTS_URL = "https://www.reddit.com/top/.json";

    GetTopPostsTask() {
        super.execute(TOP_POSTS_URL);
    }

    GetTopPostsTask(int limit, String after) {
        String url = TOP_POSTS_URL + "?&limit=" + Integer.toString(limit);

        if (after != null)
            url += "&after=" + after;

        super.execute(url);
    }

    abstract void onError();
    abstract void onSuccess(Listing result);
}
