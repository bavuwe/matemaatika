package com.bavuwe.matemaatika.listeners;

import android.view.View;
import android.widget.AdapterView;

import com.bavuwe.matemaatika.Matemaatika;

public class SubTopicOnListClickListenerImpl implements AdapterView.OnItemClickListener {
    private SubtopicEmitterListener listener;
    private int classIdx;

    public SubTopicOnListClickListenerImpl(SubtopicEmitterListener listener, final int classIdx) {
        this.listener = listener;
        this.classIdx = classIdx;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        boolean isHeader = Matemaatika.isFlattenedHeader[this.classIdx][position];
        if (!isHeader) {
            int subtopicIdx = Matemaatika.flattenedSubtopics[classIdx][position];
            listener.handleEmittedSubtopic(subtopicIdx);
        }
    }
}
