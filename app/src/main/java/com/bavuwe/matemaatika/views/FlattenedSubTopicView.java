package com.bavuwe.matemaatika.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bavuwe.matemaatika.R;

public class FlattenedSubTopicView extends LinearLayout {
    TextView label;

    public FlattenedSubTopicView(final Context context, final String text) {
        super(context);
        inflateLayout(context);
        this.label = findViewById(R.id.flattened_subtopic_label);
        this.label.setText(text);
    }

    public void setText(final String text) {
        this.label.setText(text);
    }

    protected void inflateLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.flattened_subtopic, this, true);
    }
}
