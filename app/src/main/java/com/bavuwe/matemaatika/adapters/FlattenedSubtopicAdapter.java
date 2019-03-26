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
package com.bavuwe.matemaatika.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bavuwe.matemaatika.Matemaatika;
import com.bavuwe.matemaatika.views.FlattenedSubTopicView;
import com.bavuwe.matemaatika.views.FlattenedTopicView;

public class FlattenedSubtopicAdapter extends BaseAdapter {
    private final int classIdx;
    private Context context;

    public FlattenedSubtopicAdapter(final Context context, final int classIdx) {
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
        return Matemaatika.isFlattenedHeader[classIdx][position]
                ? new FlattenedTopicView(context, getItem(position))
                : new FlattenedSubTopicView(context, getItem(position));
    }
}
