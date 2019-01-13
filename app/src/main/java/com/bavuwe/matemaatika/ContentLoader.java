package com.bavuwe.matemaatika;

import android.os.AsyncTask;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class used to load the matemaatika content and index asyncronously.
 */
class ContentLoader extends AsyncTask<String, Integer, String> {
    MataActivity activity;

    /**
     * Constructor.
     * @param activity The activity to notify when the loading is done!
     */
    public ContentLoader(MataActivity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        if (Matemaatika.classTitles == null) {
            //read content from mata.html
            MataHTMLParser parser = null;
            try {
                InputStream structIs = activity.getAssets().open("mata.structure");
                InputStream htmlIs = activity.getAssets().open("mata.html");
                InputStream indexIs = activity.getAssets().open("mata.index");

                parser = new MataHTMLParser(structIs, htmlIs);
                // get the titles
                Matemaatika.classTitles = parser.getClassTitles();
                Matemaatika.topicTitles = parser.getTopicTitles();
                Matemaatika.subTopicTitles = parser.getSubTopicTitles();
                // get the tree structure
                Matemaatika.classTopics = new int[Matemaatika.classTitles.length][];
                for (int classIdx=0 ; classIdx < Matemaatika.classTitles.length ; ++classIdx) {
                    Matemaatika.classTopics[classIdx] = parser.getClassTopics(classIdx);
                }
                Matemaatika.topicSubTopics = new int[Matemaatika.topicTitles.length][];
                for (int topicIdx=0 ; topicIdx < Matemaatika.topicTitles.length ; ++topicIdx) {
                    Matemaatika.topicSubTopics[topicIdx] = parser.getTopicSubTopics(topicIdx);
                }
                // get the subtopic contents
                Matemaatika.subTopicContents = new String[Matemaatika.subTopicTitles.length];

                for (int subTopicIdx=0 ; subTopicIdx < Matemaatika.subTopicTitles.length ; ++subTopicIdx) {
                    Matemaatika.subTopicContents[subTopicIdx] = parser.getSubTopicHTML(subTopicIdx);
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
        new Handler(activity.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                activity.contentLoaded();
                activity.removeDialog(MataActivity.LOAD_DIALOG);
            }
        });
        return null;
    }
}