package com.bavuwe.matemaatika;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
    public Object getItem(int position) {
        return Matemaatika.flattenedTitles[classIdx][position];
    }

    @Override
    public long getItemId(int position) {
        return Matemaatika.flattenedSubtopics[classIdx][position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view;
        if (convertView != null) {
            view = (TextView) convertView;
        } else {
            view = new TextView(context);
            view.setText(Matemaatika.flattenedTitles[classIdx][position]);
        }
        return view;
    }
}
