# Android Reddit Reader [![Build Status](https://travis-ci.org/mauguignard/androidRedditReader.svg?branch=master)](https://travis-ci.org/mauguignard/androidRedditReader)

## Context

This project is being carried out by [Mauricio Guignard](https://github.com/mauguignard) for elective _"Android Programming: Introduction"_ of the Faculty of Mathematics, Astronomy, Physics and Computing (FaMAF), UNC, Argentina dictated by Professor [Diego Mercado](https://github.com/mercadodiego).

## Activities Assignment | Step 1

### Objectives

* Learn the communication between activities and their lifecycle

### Statement

* Download the tag "activities_assignment" from the repository https://github.com/mercadodiego/RedditReader
* Pressing "Sign in or register" should invoke `LoginActivity`
* Once logged in, the user name should be displayed on the screen

## LayoutAssignment | Step 2

### Objectives

* Learn the basic principles of Layouts, View and View Groups on Android
* Learn to modify and add graphics and text resources to the application

### Statement

* Download the tag "layout_assignment" from the repository https://github.com/mercadodiego/RedditReader
* Add Spanish translation
* Change application name to "Reddit Reader" in English and "Lector de Reddit" in Spanish
* Change application icon to ./images/reddit_icon.png
* Change package name to ar.edu.unc.famaf.redditreader
* Add a subreddit item layout as shown in ./images/screenshot1.jpg

## AdaptersAssignment | Step 3

### Objectives

* Implement a ListView that gets its content from a custom ArrayAdapter

### Statement

* Download the tag "adapters_assignment" from the repository https://github.com/mercadodiego/RedditReader
* The class `ar.edu.unc.famaf.redditreader.model.PostModel` should represent a Post in Reddit. Add the attributes relating to title, author, creation date, number of comments, _image preview / thumbnail_, etc. with the corresponding _setter / getter_
* Implement the method `List<PostModel> getTopPosts()` of the class `ar.edu.unc.famaf.redditreader.backend.Backend`. It should always return 5 dummy PostModel instances
* Create a `ar.edu.unc.famaf.redditreader.ui.PostAdapter` class that extends `android.widget.ArrayAdapter` and re-implement the necessary methods
* `NewsActivityFragment` should show a ListView occupying all the space available and must display the contents of each of the posts following the design implemented in the previous activity _LayoutAssignment_. Keep in mind that the title should always be displayed and the height of each row should be adjusted to allow it
* Implement a ViewHolder in the `ar.edu.unc.famaf.redditreader.ui.PostAdapter` class to improve the performance of the ListView

## ThreadsAssignment | Step 4

### Objectives

* Implement an AsyncTask within the ListView to download the _thumbnails_ from Internet

### Prerequisites

* Having completed the _AdaptersAssignment_ activity

### Statement

* The class `ar.edu.unc.famaf.redditreader.ui.PostAdapter` should implement an AsyncTask that given an URL previously defined in the `ar.edu.unc.famaf.redditreader.model.PostModel` class, allows to download the image and displays it in the ImageView representative of the _image preview / thumbnail_
* An animated `android.widget.ProgressBar` must be shown while the image is being downloaded

## WebServicesAssignment | Step 5

### Objectives

* Make a call to a REST service, interpret the returned JSON and display the results on screen.

### Prerequisites

* Having completed the _ThreadsAssignment_ activity

### Statement

* The class `ar.edu.unc.famaf.redditreader.backend.GetTopPostsTask` should get the content of the first 50 Reddit's Top posts via HTTP in JSON format, interpret it and return as result a `List<PostModel>`
* The content must be displayed in the _ListView_ of the `NewsActivityFragment` class
* When no INTERNET connection is available, an _AlertDialog_ or _Toast_ must be displayed indicating the error
* The JSON interpreter must be defined in a new `ar.edu.unc.famaf.redditreader.backend.Parser` class and must implement the following input method, internally using an instance of _JsonReader_ that returns an instance of a new class called `ar.edu.unc.famaf.redditreader.model.Listing` (according to the structure of objects of the Reddit API)
```Java
    public Listing readJsonStream(InputStream in) throws IOException {....}
```

## PersistenceAssignment | Step 6

### Objectives

* Implement a small SQLite database

### Prerequisites

* Having completed the _WebServicesAssignment_ activity

### Statement

* The class `ar.edu.unc.famaf.redditreader.backend.GetTopPostsTask` should implement the following behaviour:
 1. Invoke Reddit's REST service to get the 50 first TOP posts
 2. Persist the results in an internal database
 3. Return the results from the internal database
 4. If no internet connection is available, the last results obtained from the internal database must be returned
* The internal database must be implemented in a new class `ar.edu.unc.famaf.redditreader.backend.RedditDBHelper` that extends `SQLiteOpenHelper`
 * Should only store the last 50 posts. All the others must be deleted
* The _thumbnails/preview_ should also be stored in the internal database as they are downloaded. Remember that they can be stored as a byte array:
```Java
   public static byte[] getBytes(Bitmap bitmap)
   {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG,0, stream);
        return stream.toByteArray();
   }
   public static Bitmap getImage(byte[] image)
   {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
   }
```

## Licence

* [Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International Public License](https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode)
