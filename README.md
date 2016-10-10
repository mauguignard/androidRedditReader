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
