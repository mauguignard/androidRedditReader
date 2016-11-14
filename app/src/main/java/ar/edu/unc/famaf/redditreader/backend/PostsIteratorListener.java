package ar.edu.unc.famaf.redditreader.backend;

import java.util.List;

import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by mauguignard on 10/28/16.
 */

public interface PostsIteratorListener {
    void nextPosts(List<PostModel> posts);
}