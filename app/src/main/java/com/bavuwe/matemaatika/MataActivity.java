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
package com.bavuwe.matemaatika;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bavuwe.matemaatika.adapters.FlattenedSubtopicAdapter;
import com.bavuwe.matemaatika.adapters.SearchResultsAdapter;
import com.bavuwe.matemaatika.listeners.ContentLoaderListener;
import com.bavuwe.matemaatika.listeners.SearchResultOnClickListener;
import com.bavuwe.matemaatika.listeners.SubTopicOnListClickListenerImpl;
import com.bavuwe.matemaatika.listeners.SubtopicEmitterListener;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

public class MataActivity extends AppCompatActivity implements
        ContentLoaderListener, SubtopicEmitterListener {

    static final int LOAD_DIALOG = 0;
    static final String SELECTED_CLASS_KEY = "selected_class";

    static final int DRAWER_FAVOURITES_IDENTIFIER = 1;
    static final int DRAWER_CLASS1_IDENTIFIER = 2;
    static final int DRAWER_CLASS2_IDENTIFIER = 3;
    static final int DRAWER_CLASS3_IDENTIFIER = 4;
    static final int DRAWER_CLASS4_IDENTIFIER = 5;
    static final int DRAWER_CLASS5_IDENTIFIER = 6;
    static final int DRAWER_CLASS6_IDENTIFIER = 7;
    static final int DRAWER_CLASS7_IDENTIFIER = 8;
    static final int DRAWER_CLASS8_IDENTIFIER = 9;
    static final int DRAWER_CLASS9_IDENTIFIER = 10;
    static final int DRAWER_GYMNASIUM_IDENTIFIER = 11;
    static final int DRAWER_COMPUND_INTEREST_IDENTIFIER = 12;
    static final int DRAWER_QUADRATIC_IDENTIFIER = 13;
    static final int DRAWER_REPORT_IDENTIFIER = 14;
    static final int DRAWER_ABOUT_IDENTIFIER = 15;

    ContentLoader loader = null;
    Drawer drawer = null;
    ListView listView = null;
    WebView webView = null;
    ListView searchResults = null;
    SearchResultsAdapter searchResultsAdapter = null;
    SearchView searchView = null;
    Toolbar toolbar = null;
    Integer selectedSubTopic = null;

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (selectedSubTopic != null) {
            state.putInt(SELECTED_CLASS_KEY, selectedSubTopic);
        }
        selectedSubTopic = null;
    }

    @Override
    public void onRestoreInstanceState(Bundle state) {
        Object value = state.get(SELECTED_CLASS_KEY);
        if (value != null) {
            selectedSubTopic = (Integer)value;
            handleEmittedSubtopicAndSelectClass(selectedSubTopic);
        }
        setStatusBarColor();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mata);

        listView = findViewById(R.id.list_view);
        searchResults = findViewById(R.id.search_results);
        searchResultsAdapter = new SearchResultsAdapter(getApplicationContext());
        searchResults.setAdapter(searchResultsAdapter);
        searchResults.setOnItemClickListener(new SearchResultOnClickListener(this));

        // Set up webview and its settings.
        webView = findViewById(R.id.webview);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);

        onCreateToolbar();
        onCreateActionbar();
        onCreateDrawer(savedInstanceState);
        setStatusBarColor();

        // Kick off contentloader.
        if (Matemaatika.classTitles == null) {
            showDialog(LOAD_DIALOG);
            loader = new ContentLoader(this, getAssets());
            loader.execute();
        }
    }

    void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int startColor = getWindow().getStatusBarColor();
            int endColor = ContextCompat.getColor(getApplicationContext(), R.color.mata_statusbar_color);
            ObjectAnimator.ofArgb(getWindow(), "statusBarColor", startColor, endColor).start();
        }
    }

    void onCreateToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    void onCreateActionbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    void onCreateDrawer(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withRootView(R.id.main_layout)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(false)
                .withDrawerLayout(R.layout.material_drawer_fits_not)
                .addDrawerItems(
                        //new PrimaryDrawerItem().withName("Lemmikud").withIcon(GoogleMaterial.Icon.gmd_star).withIdentifier(DRAWER_FAVOURITES_IDENTIFIER),
                        //new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(getString(R.string.class_1)).withIcon(GoogleMaterial.Icon.gmd_filter_1).withIdentifier(DRAWER_CLASS1_IDENTIFIER),
                        new PrimaryDrawerItem().withName(getString(R.string.class_2)).withIcon(GoogleMaterial.Icon.gmd_filter_2).withIdentifier(DRAWER_CLASS2_IDENTIFIER),
                        new PrimaryDrawerItem().withName(getString(R.string.class_3)).withIcon(GoogleMaterial.Icon.gmd_filter_3).withIdentifier(DRAWER_CLASS3_IDENTIFIER),
                        new PrimaryDrawerItem().withName(getString(R.string.class_4)).withIcon(GoogleMaterial.Icon.gmd_filter_4).withIdentifier(DRAWER_CLASS4_IDENTIFIER),
                        new PrimaryDrawerItem().withName(getString(R.string.class_5)).withIcon(GoogleMaterial.Icon.gmd_filter_5).withIdentifier(DRAWER_CLASS5_IDENTIFIER),
                        new PrimaryDrawerItem().withName(getString(R.string.class_6)).withIcon(GoogleMaterial.Icon.gmd_filter_6).withIdentifier(DRAWER_CLASS6_IDENTIFIER),
                        new PrimaryDrawerItem().withName(getString(R.string.class_7)).withIcon(GoogleMaterial.Icon.gmd_filter_7).withIdentifier(DRAWER_CLASS7_IDENTIFIER),
                        new PrimaryDrawerItem().withName(getString(R.string.class_8)).withIcon(GoogleMaterial.Icon.gmd_filter_8).withIdentifier(DRAWER_CLASS8_IDENTIFIER),
                        new PrimaryDrawerItem().withName(getString(R.string.class_9)).withIcon(GoogleMaterial.Icon.gmd_filter_9).withIdentifier(DRAWER_CLASS9_IDENTIFIER),
                        new PrimaryDrawerItem().withName(getString(R.string.class_gymnaasium)).withIcon(GoogleMaterial.Icon.gmd_school).withIdentifier(DRAWER_GYMNASIUM_IDENTIFIER),
                        //new DividerDrawerItem(),
                        //new PrimaryDrawerItem().withName("Liitintresside kalkulaator").withIcon(GoogleMaterial.Icon.gmd_trending_up).withIdentifier(DRAWER_COMPUND_INTEREST_IDENTIFIER), // need better icon here
                        //new PrimaryDrawerItem().withName("Ruutvõrrandi lahendaja").withIcon(GoogleMaterial.Icon.gmd_swap_calls).withIdentifier(DRAWER_QUADRATIC_IDENTIFIER), // need better icon here
                        new DividerDrawerItem(),
                        //new PrimaryDrawerItem().withName("Teavita veast").withIcon(GoogleMaterial.Icon.gmd_notifications_active).withIdentifier(DRAWER_REPORT_IDENTIFIER),
                        new PrimaryDrawerItem().withName(getString(R.string.about)).withIcon(GoogleMaterial.Icon.gmd_pets).withIdentifier(DRAWER_ABOUT_IDENTIFIER)
                ) // Add the items we want to use with our Drawer.
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            int identifier = (int) drawerItem.getIdentifier();
                            if (identifier >= DRAWER_CLASS1_IDENTIFIER && identifier <= DRAWER_GYMNASIUM_IDENTIFIER) {
                                final int classIdx = identifier - DRAWER_CLASS1_IDENTIFIER;
                                selectClass(classIdx);
                                handleEmittedSubtopic(Matemaatika.topicSubTopics[Matemaatika.classTopics[classIdx][0]][0]);
                                drawer.closeDrawer();
                                return true;
                            } else if (identifier == DRAWER_ABOUT_IDENTIFIER) {
                                loadAboutHtml();
                            }
                        }
                        selectedSubTopic = null;
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();
    }

    void selectClass(final int classIdx) {
        listView.setAdapter(new FlattenedSubtopicAdapter(getApplicationContext(), classIdx));
        listView.setOnItemClickListener(new SubTopicOnListClickListenerImpl(this, classIdx));
    }

    // this method will be called from another thread
    public void contentLoaded() {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                removeDialog(MataActivity.LOAD_DIALOG);
                Toast.makeText(getApplicationContext(), "content loaded", Toast.LENGTH_LONG).show();

                // by default, show the first topic of the first class
                selectClass(0);
                handleEmittedSubtopic(0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(searchResultsAdapter);
        searchResultsAdapter.setSearchResultsView(searchResults);

        return true;
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
        searchView = null;
    }

    @Override
    public void handleEmittedSubtopic(int subTopicIdx) {
        selectedSubTopic = subTopicIdx;
        toolbar.setTitle(Matemaatika.searchTitles[subTopicIdx]);

        String content = Matemaatika.subTopicContents[subTopicIdx];
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.loadDataWithBaseURL("file:///android_asset/", content, "text/html","utf-8", null);

        // clear the search
        if (searchView != null) {
            searchView.setQuery("", true);
            searchView.clearFocus();
        }
        if (toolbar != null) {
            toolbar.collapseActionView();
        }
    }

    @Override
    public void handleEmittedSubtopicAndSelectClass(int subTopicIdx) {
        handleEmittedSubtopic(subTopicIdx);
        int classIdx = Matemaatika.subTopicClass[subTopicIdx];
        selectClass(classIdx);

        int flattenedIdx = Matemaatika.findFlattenedIndex(classIdx, subTopicIdx);
        listView.setSelection(flattenedIdx);
        listView.setItemChecked(flattenedIdx, true);
    }

    void loadAboutHtml() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
        webView.loadUrl("file:///android_asset/about.html");
    }
}

