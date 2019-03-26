/*
 * This file is part of the Matemaatika Minileksikon.
 * https://github.com/bavuwe/matemaatika
 *
 * Copyright (c) 2019 Bavuwe Software and contributors.
 * See CONTRIBUTORS.txt for details.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bavuwe.matemaatika;

import android.app.Application;

/**
 * Matemaatika application i.e global state.
 */
public class Matemaatika extends Application {
    public static String[] classTitles = null;
    public static String[] topicTitles = null;
    public static String[] subTopicTitles = null;
    public static String[] searchTitles = null;

    // Reverse indexes for mapping subtopic into class and topic.
    public static int[] subTopicClass = null;
    public static int[] subTopicTopic = null;

    /**
     * HTML contents of the subtopics, all loaded in memory.
     */
    public static String[] subTopicContents = null;

    /**
     * Denotes, which listView belong to which classes.
     */
    public static int[][] classTopics = null;

    /**
     * Denotes, which subtopics belong to which listView.
     */
    public static int[][] topicSubTopics = null;

    /**
     * The index for performing searches.
     */
    public static ContentIndex index = null;

    // We are going to also need flattened subtopics for display purposes.
    public static Integer[][] flattenedSubtopics = null;
    public static Boolean[][] isFlattenedHeader = null;
    public static String[][] flattenedTitles = null;

    // TODO: refactor using Optional instead of return -1
    static int findFlattenedIndex(int classIdx, int subtopic) {
        for (int idx=0 ; idx<Matemaatika.flattenedSubtopics[classIdx].length ; ++idx) {
            if (!Matemaatika.isFlattenedHeader[classIdx][idx] && Matemaatika.flattenedSubtopics[classIdx][idx] == subtopic) {
                return idx;
            }
        }
        return -1;
    }
}