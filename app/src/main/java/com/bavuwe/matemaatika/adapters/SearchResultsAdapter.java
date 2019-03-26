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
import android.widget.ListView;

import androidx.appcompat.widget.SearchView;

import com.bavuwe.matemaatika.Matemaatika;
import com.bavuwe.matemaatika.views.SearchResult;

public class SearchResultsAdapter extends BaseAdapter implements SearchView.OnQueryTextListener {
    private Context context;
    private int[] matches = new int[]{};
    private ListView searchResultsView = null;

    public SearchResultsAdapter(final Context context) {
        this.context = context;
    }

    public void setSearchResultsView(final ListView listView) {
        searchResultsView = listView;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        this.matches = Matemaatika.index.query(query);
        if (searchResultsView != null) {
            notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public int getCount() {
        return matches.length;
    }

    @Override
    public String getItem(int position) {
        return Matemaatika.searchTitles[matches[position]];
    }

    @Override
    public long getItemId(int position) {
        return matches[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO: don't ignore convertView here and create new instance every time
        return new SearchResult(context, Matemaatika.searchTitles[matches[position]]);
    }
}
