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
package com.bavuwe.matemaatika.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bavuwe.matemaatika.R;

public class SearchResult extends LinearLayout {
    TextView label;

    public SearchResult(final Context context, final String text) {
        super(context);
        inflateLayout(context);
        this.label = findViewById(R.id.search_result_label);
        this.label.setText(text);
    }

    public void setText(final String text) {
        this.label.setText(text);
    }

    protected void inflateLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.search_result, this, true);
    }
}
