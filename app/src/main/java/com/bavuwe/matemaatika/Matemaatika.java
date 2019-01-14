package com.bavuwe.matemaatika;

import android.app.Application;

/**
 * Matemaatika application i.e global state.
 */
public class Matemaatika extends Application {
    public static String[] classTitles = null;
    public static String[] topicTitles = null;
    public static String[] subTopicTitles = null;

    /**
     * HTML contents of the subtopics, all loaded in memory.
     */
    public static String[] subTopicContents = null;

    /**
     * Denotes, which topics belong to which classes.
     */
    public static int[][] classTopics = null;

    /**
     * Denotes, which subtopics belong to which topics.
     */
    public static int[][] topicSubTopics = null;

    /**
     * The index for performing searches.
     */
    public static ContentIndex index = null;

}