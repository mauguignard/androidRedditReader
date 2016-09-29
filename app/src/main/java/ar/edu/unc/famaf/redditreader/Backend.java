package ar.edu.unc.famaf.redditreader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mauguignard on 9/29/16.
 */
public class Backend {
    private static Backend ourInstance = new Backend();

    public static Backend getInstance() {
        return ourInstance;
    }

    private List<PostModel> mLstPostsModel;
    private Backend() {
        mLstPostsModel = new ArrayList<>();

        String[] subredditNames = {
                "r/funny",
                "r/funny",
                "r/creepy",
                "r/gifts",
                "r/pics"
        };

        String[] subredditDescriptions = {
                "Fantastic Name.",
                "Conan on sexism",
                "I asked my sculptor friend to make me something creepy using my grandfather's glass eye. He did.",
                "Somebody's got a spring in their step",
                "I stood here for an hour, in Mountain Lion country, waiting for the galaxy to align with the road. It was totally worth it."
        };

        String[] subredditIcons = {
                "https://b.thumbs.redditmedia.com/g3SGuAZgUFRZffXusvn9W8mgAQzzz-zx25rpw6fYeWA.jpg",
                "https://a.thumbs.redditmedia.com/1xhcjetJ67YFk6Udip4PE1K5_k6goJrgV1IkkCiMGg0.jpg",
                "https://b.thumbs.redditmedia.com/Saurgod807X2u6NgheWeM0dzjZshXIm4Es1xHcah6bA.jpg",
                "https://b.thumbs.redditmedia.com/POb0KpNagq07j8M5ZMzl0zyJqUpRRZHUG_S6Mae8yJc.jpg",
                "https://b.thumbs.redditmedia.com/ns-cFGDY5srMZ616vG3eL5OmGMd_s7Yo1qKpaqJJ7GQ.jpg"
        };

        int[] subredditNoComments = {
                153,
                396,
                154,
                553,
                601
        };

        long[] subredditDates = {
                System.currentTimeMillis() - 46800000,
                System.currentTimeMillis() - 14400000,
                System.currentTimeMillis() - 10800000,
                System.currentTimeMillis() - 32400000,
                System.currentTimeMillis() - 14400000
        };

        for (int i = 0; i < 5; i++) {
            PostModel item = new PostModel();
            item.setSubreddit(subredditNames[i]);
            item.setDescription(subredditDescriptions[i]);
            item.setNoComments(subredditNoComments[i]);
            item.setDate(subredditDates[i]);
            item.setIconURL(subredditIcons[i]);
            mLstPostsModel.add(item);
        }
    }

    public  List<PostModel> getTopPosts() {
        return mLstPostsModel;
    }
}
