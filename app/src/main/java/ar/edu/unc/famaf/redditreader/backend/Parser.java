package ar.edu.unc.famaf.redditreader.backend;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unc.famaf.redditreader.model.Listing;
import ar.edu.unc.famaf.redditreader.model.PostModel;

/**
 * Created by mauguignard on 10/22/16.
 */

class Parser {
    static class UnexpectedResponse extends Exception {
        UnexpectedResponse(String message) {
            super(message);
        }
    }

    static Listing readJsonStream(InputStream in) throws IOException, UnexpectedResponse {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readListing(reader);
        } finally {
            reader.close();
        }
    }

    private static Listing readListing(JsonReader reader) throws IOException, UnexpectedResponse {
        reader.beginObject();

        if (reader.hasNext()) {
            if (reader.nextName().equals("kind") && reader.nextString().equals("Listing")) {
                if (reader.hasNext() && reader.nextName().equals("data")) {
                    return readListingData(reader);
                }
            }
        }
        throw new UnexpectedResponse("Response has no Listing!");
    }

    private static Listing readListingData(JsonReader reader)
            throws IOException, UnexpectedResponse {
        List<PostModel> children = null;
        String after = null;
        String before = null;

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "children":
                    if (reader.peek() != JsonToken.NULL)
                        children = readListingChildren(reader);
                    else
                        throw new UnexpectedResponse("Response has no Posts!");
                    break;
                case "after":
                    if (reader.peek() != JsonToken.NULL)
                        after = reader.nextString();
                    else
                        reader.skipValue();
                    break;
                case "before":
                    if (reader.peek() != JsonToken.NULL)
                        before = reader.nextString();
                    else
                        reader.skipValue();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return new Listing(children, after, before);
    }

    private static List<PostModel> readListingChildren(JsonReader reader)
            throws IOException, UnexpectedResponse {
        List<PostModel> posts = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            if (reader.hasNext()) {
                if (reader.nextName().equals("kind") && reader.nextString().equals("t3")) {
                    if (reader.hasNext() && reader.nextName().equals("data")) {
                        posts.add(readPost(reader));
                    }
                }
            }
            reader.endObject();
        }
        reader.endArray();

        return posts;
    }

    private static PostModel readPost(JsonReader reader) throws IOException, UnexpectedResponse {
        if (reader.peek() == JsonToken.NULL)
            throw new UnexpectedResponse("Empty Post data!");

        PostModel post = new PostModel();

        reader.beginObject();
        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "domain":
                    post.setDomain(reader.nextString());
                    break;
                case "subreddit":
                    post.setSubreddit(reader.nextString());
                    break;
                case "gilded":
                    post.setGilded(reader.nextInt());
                    break;
                case "author":
                    post.setAuthor(reader.nextString());
                    break;
                case "name":
                    post.setName(reader.nextString());
                    break;
                case "score":
                    post.setScore(reader.nextInt());
                    break;
                case "over_18":
                    post.setOver18(reader.nextBoolean());
                    break;
                case "thumbnail":
                    post.setThumbnail(reader.nextString());
                    break;
                case "permalink":
                    post.setPermalink(reader.nextString());
                    break;
                case "created_utc":
                    post.setCreated(reader.nextLong());
                    break;
                case "link_flair_text":
                    if (reader.peek() != JsonToken.NULL)
                        post.setLinkFlairText(reader.nextString());
                    else
                        reader.skipValue();
                    break;
                case "url":
                    post.setURL(reader.nextString());
                    break;
                case "title":
                    post.setTitle(reader.nextString());
                    break;
                case "num_comments":
                    post.setNoComments(reader.nextInt());
                    break;
                case "downs":
                    post.setDowns(reader.nextInt());
                    break;
                case "up":
                    post.setUps(reader.nextInt());
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return post;
    }

}

