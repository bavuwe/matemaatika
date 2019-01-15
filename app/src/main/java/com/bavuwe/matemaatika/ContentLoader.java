package com.bavuwe.matemaatika;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;

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
        listener.contentLoaded();
        return null;
    }
}