package ar.edu.unc.famaf.redditreader.backend;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.R;
import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by mauguignard on 9/29/16.
 */
public class Backend {
    private static Backend ourInstance = new Backend();

    public static Backend getInstance() {
        return ourInstance;
    }

    private final List<PostModel> mLstPostsModel;

    private Backend() {
        mLstPostsModel = new ArrayList<>();

        String[] domains = {
                "i.imgur.com", "i.imgur.com", "aclu.org", "ibtimes.co.uk", "imgur.com"
        };

        String[] subreddits = {
                "funny", "aww", "EvangelionUnit00", "worldnews", "gifs"
        };

        Integer[] gilded = {
                0, 0, 5, 0, 2
        };

        String[] authors = {
                "bengaldude545", "rahul8aggarwal", "el_matto", "SparklyPen", "SlimJones123"
        };

        String[] thumbnails = {
                Integer.toString(R.drawable.thumbnail1),
                Integer.toString(R.drawable.thumbnail2),
                Integer.toString(R.drawable.thumbnail3),
                Integer.toString(R.drawable.ic_self_icon),
                Integer.toString(R.drawable.thumbnail5)
        };

        Long[] created = {
                1476098795L, 1476139970L, 1476100953L, 1476110887L, 1476099541L
        };

        String[] titles = {
                "Bird thinks guy is a tree",
                "Henceforth, he lived happily ever after...",
                "TIL Thousands of prisoners were abandoned in Orleans Parish Prison during Hurricane Katrina where the water and sewage rose up to neck deep. They went days without food, water or ventilation.",
                "Philippines President Duterte orders US forces out after 65 years: 'Do not treat us like a doormat'",
                "Looks like we got another stow away"
        };

        Integer[] noComments = {
                1180, 882, 1134, 5179, 1191
        };

        for (int i = 0; i < 5; i++) {
            PostModel item = new PostModel();
            item.setDomain(domains[i]);
            item.setSubreddit(subreddits[i]);
            item.setGilded(gilded[i]);
            item.setAuthor(authors[i]);
            item.setThumbnail(thumbnails[i]);
            item.setCreated(created[i]);
            item.setTitle(titles[i]);
            item.setNoComments(noComments[i]);
            mLstPostsModel.add(item);
        }
    }

    public List<PostModel> getTopPosts() {
        return mLstPostsModel;
    }

}