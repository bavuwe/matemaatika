package com.bavuwe.matemaatika;

import android.app.Application;
import android.content.SharedPreferences;

public class Matemaatika extends Application {
    static final int LOAD_DIALOG = 0;

    // the titles of the different topics
    public static String[] classTitles = null;
    public static String[] topicTitles = null;
    public static String[] subTopicTitles = null;

    // the HTML content of the subtopics
    public static String[] subTopicContents = null;

    // which topics are in what classes
    // and what subtopics in what topics
    public static int[][] classTopics = null;
    public static int[][] topicSubTopics = null;

    // the index
    public static ContentIndex index = null;


    /**
     * Should we show the introduction tutorial to the user?
     */
    public boolean showTutorial() {
        SharedPreferences prefs = getSharedPreferences("Matemaatika", 0);
        return Boolean.parseBoolean(prefs.getString("showTutorial", "true"));
    }

    /**
     * Set if we should show the introduction to the user.
     * @param b
     */
    public void setShowTutorial(boolean b) {
        SharedPreferences prefs = getSharedPreferences("Matemaatika", 0);
        prefs.edit().putBoolean("showTutorial", true);
    }

}