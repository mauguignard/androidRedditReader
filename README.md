# Android Reddit Reader [![Build Status](https://travis-ci.org/mauguignard/androidRedditReader.svg?branch=master)](https://travis-ci.org/mauguignard/androidRedditReader)

## Context

This project is being carried out by [Mauricio Guignard](https://github.com/mauguignard) for elective _"Android Programming: Introduction"_ of the Faculty of Mathematics, Astronomy and Physics (FaMAF), UNC, Argentina dictated by Professor [Diego Mercado](https://github.com/mercadodiego).

## Activities Assignment | Step 1

### Objetives

* Learn the communication between activities and their lifecycle

### Statement

* Download the tag "activities_assignment" from the repository https://github.com/mercadodiego/RedditReader
* Pressing "Sign in or register" should invoke LoginActivity.
* Once logged in, the user name should be displayed on the screen

## LayoutAssignment | Step 2

### Objetives

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

### Objetives

* Implement a ListView that gets its content from a custom ArrayAdapter

### Statement

* Download the tag "adapters_assignment" from the repository https://github.com/mercadodiego/RedditReader
* The class _ar.edu.unc.famaf.redditreader.model.PostModel_ should represent a Post in Reddit. Add the attributes relating to title, author, creation date, number of comments, _image preview / thumbnail_, etc. with the corresponding _setter / getter_
* Implement the method _List<PostModel> getTopPosts()_ of the class _ar.edu.unc.famaf.redditreader.backend.Backend_. It should always return 5 dummy PostModel instances
* Create a _ar.edu.unc.famaf.redditreader.ui.PostAdapter_ class that extends android.widget.ArrayAdapter and re-implement the necessary methods
* NewsActivityFragment should show a ListView occupying all the space available and must display the contents of each of the posts following the design implemented in the previous activity _LayoutAssignment_. Keep in mind that the title should always be displayed and the height of each row should be adjusted to allow it
* Implement a ViewHolder in the _ar.edu.unc.famaf.redditreader.ui.PostAdapter_ class to improve the performance of the ListView

## ThreadsAssignment | Step 4

### Objetives

* Implement an AsyncTask within the ListView to download the _thumbnails_ from Internet

### Prerequisites

* Having completed the AdaptersAssignment activity

### Statement

* The class _ar.edu.unc.famaf.redditreader.ui.PostAdapter_ should implement an AsyncTask that given an URL previously defined in the _ar.edu.unc.famaf.redditreader.model.PostModel_ class, allows to download the image and displays it in the ImageView representative of the _image preview / thumbnail_
* An animated _android.widget.ProgressBar_ must be shown while the image is being downloaded

## WebServicesAssignment | Step 4

### Objetives

* Make a call to a REST service, interpret the returned JSON and display the results on screen.

### Prerequisites

* Having completed the ThreadsAssignment activity

### Statement

* The class _ar.edu.unc.famaf.redditreader.backend.GetTopPostsTask_ should get the content of the first 50 Reddit's Top posts via HTTP in JSON format, interpret it and return as result a List<PostModel>
* The content must be displayed in the _ListView_ of the _NewsActivyFragment_ class
* When no INTERNET connection is available, an _AlertDialog_ or _Toast_ must be displayed indicating the error
* The JSON interpreter must be defined in a new _ar.edu.unc.famaf.redditreader.backend.Parser_ class and must implement the following input method, internally using an instance of _JsonReader_ that returns an instance of a new class called _ar.edu.unc.famaf.redditreader.model.Listing_ (according to the structure of objects of the Reddit API)
```Java
    public Listing readJsonStream(InputStream in) throws IOException {....}
```

## Licence

* [Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International Public License](https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode)
