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
