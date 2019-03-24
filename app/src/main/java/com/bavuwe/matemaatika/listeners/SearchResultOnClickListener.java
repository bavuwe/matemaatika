package com.bavuwe.matemaatika.listeners;

import android.view.View;
import android.widget.AdapterView;

public class SearchResultOnClickListener implements AdapterView.OnItemClickListener {
    private SubtopicEmitterListener listener;

    public SearchResultOnClickListener(final SubtopicEmitterListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listener.handleEmittedSubtopic((int)id);
    }
}
