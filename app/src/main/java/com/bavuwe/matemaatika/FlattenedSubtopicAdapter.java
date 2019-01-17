package com.bavuwe.matemaatika;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bavuwe.matemaatika.views.FlattenedSubTopicView;
import com.bavuwe.matemaatika.views.FlattenedTopicView;

/**
 * Topic adapter, which takes data directly from Matemaatika global state.
 */
public class FlattenedSubtopicAdapter extends BaseAdapter {
    private final int classIdx;
    private Context context;

    FlattenedSubtopicAdapter(final Context context, final int classIdx) {
        this.context = context;
        this.classIdx = classIdx;
    }

    @Override
    public int getCount() {
        return Matemaatika.flattenedSubtopics[classIdx].length;
    }

    @Override
    public String getItem(int position) {
        return Matemaatika.flattenedTitles[classIdx][position];
    }

    @Override
    public long getItemId(int position) {
        return Matemaatika.flattenedSubtopics[classIdx][position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO: don't ignore convertView here and create new instance every time
        return Matemaatika.isFlattenedHeader[classIdx][position]
                ? new FlattenedTopicView(context, getItem(position))
                : new FlattenedSubTopicView(context, getItem(position));
    }
}
