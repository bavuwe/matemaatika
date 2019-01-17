package com.bavuwe.matemaatika;

import android.content.res.AssetManager;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to load the content and index asyncronously.
 */
class ContentLoader extends AsyncTask<String, Integer, String> {
    private ContentLoaderListener listener ;
    private AssetManager manager;

    /**
     * Set up a ContentLoader
     * @param listener The instance to notify loading related events.
     * @param manager For retrieving assets.
     */
    ContentLoader(ContentLoaderListener listener, AssetManager manager) {
        this.listener = listener;
        this.manager = manager;
    }

    @Override
    protected String doInBackground(String... params) {
        if (Matemaatika.classTitles == null) {
            //read content from mata.html
            MataHTMLParser parser = null;
            try {
                InputStream structIs = manager.open("mata.structure");
                InputStream htmlIs = manager.open("mata.html");
                InputStream indexIs = manager.open("mata.index");

                parser = new MataHTMLParser(structIs, htmlIs);
                // get the titles
                Matemaatika.classTitles = parser.getClassTitles();
                Matemaatika.topicTitles = parser.getTopicTitles();
                Matemaatika.subTopicTitles = parser.getSubTopicTitles();
                // get the tree structure
                Matemaatika.classTopics = new int[Matemaatika.classTitles.length][];
                for (int idx=0 ; idx < Matemaatika.classTitles.length ; ++idx) {
                    Matemaatika.classTopics[idx] = parser.getClassTopics(idx);
                }
                Matemaatika.topicSubTopics = new int[Matemaatika.topicTitles.length][];
                for (int idx=0 ; idx < Matemaatika.topicTitles.length ; ++idx) {
                    Matemaatika.topicSubTopics[idx] = parser.getTopicSubTopics(idx);
                }
                // get the subtopic contents
                Matemaatika.subTopicContents = new String[Matemaatika.subTopicTitles.length];

                for (int idx=0 ; idx < Matemaatika.subTopicTitles.length ; ++idx) {
                    Matemaatika.subTopicContents[idx] = parser.getSubTopicHTML(idx);
                }
                htmlIs.close();
                structIs.close();

                // load the index
                Matemaatika.index = new ContentIndex();
                Matemaatika.index.load(indexIs);
                indexIs.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // for display purposes, we need flattened topics, compute them here
        computeFlattenedTopics();
        listener.contentLoaded();
        return null;
    }

    private void computeFlattenedTopics() {
        Matemaatika.flattenedSubtopics = new Integer[Matemaatika.classTitles.length][];
        Matemaatika.isFlattenedHeader = new Boolean[Matemaatika.classTitles.length][];
        Matemaatika.flattenedTitles = new String[Matemaatika.classTitles.length][];

        for (int classIdx=0 ; classIdx<Matemaatika.classTitles.length ; ++classIdx) {
            computeFlattenedTopicsForClass(classIdx);
        }
    }

    private void computeFlattenedTopicsForClass(int classIdx) {
        List<Integer> subtopics = new ArrayList<>();
        List<Boolean> isHeader = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        int[] topics = Matemaatika.classTopics[classIdx];
        for (int topicIdx=0 ; topicIdx < topics.length ; ++topicIdx) {
            // first, add the header
            subtopics.add(-topics[topicIdx]);  // use negative topicIdx here to avoid mixup with subtopics
            isHeader.add(true);
            titles.add(Matemaatika.topicTitles[topics[topicIdx]]);

            // second, add subtopics
            for (int subtopicIdx=0 ; subtopicIdx<Matemaatika.topicSubTopics[topics[topicIdx]].length ; ++subtopicIdx) {
                subtopics.add(Matemaatika.topicSubTopics[topics[topicIdx]][subtopicIdx]);
                isHeader.add(false);
                titles.add(Matemaatika.subTopicTitles[Matemaatika.topicSubTopics[topics[topicIdx]][subtopicIdx]]);
            }
        }
        Matemaatika.flattenedSubtopics[classIdx] = subtopics.toArray(new Integer[0]);
        Matemaatika.isFlattenedHeader[classIdx] = isHeader.toArray(new Boolean[0]);
        Matemaatika.flattenedTitles[classIdx] = titles.toArray(new String[0]);
    }
}
