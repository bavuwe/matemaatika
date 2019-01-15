package com.bavuwe.matemaatika;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Topic adapter, which takes data directly from Matemaatika global state.
 */
public class TopicAdapter extends BaseAdapter {
    private final int classIdx;
    private Context context;

    TopicAdapter(final Context context, final int classIdx) {
        this.context = context;
        this.classIdx = classIdx;
    }

    @Override
    public int getCount() {
        return Matemaatika.classTopics[classIdx].length;
    }

    @Override
    public Object getItem(int position) {
        return Matemaatika.topicTitles[Matemaatika.classTopics[classIdx][position]];
    }

    @Override
    public long getItemId(int position) {
        return Matemaatika.classTopics[classIdx][position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view;
        if (convertView != null) {
            view = (TextView) convertView;
        } else {
            view = new TextView(context);
            view.setText(Matemaatika.topicTitles[Matemaatika.classTopics[classIdx][position]]);
        }
        return view;
    }
}
