package com.bavuwe.matemaatika;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.crossfader.util.UIUtils;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

public class MataActivity extends AppCompatActivity implements
        ContentLoaderListener {

    // dialogs
    static final int LOAD_DIALOG = 0;

    // drawer identifiers
    static final int DRAWER_FAVOURITES_IDENTIFIER = 0;
    static final int DRAWER_CLASS1_IDENTIFIER = 1;
    static final int DRAWER_CLASS2_IDENTIFIER = 2;
    static final int DRAWER_CLASS3_IDENTIFIER = 3;
    static final int DRAWER_CLASS4_IDENTIFIER = 4;
    static final int DRAWER_CLASS5_IDENTIFIER = 5;
    static final int DRAWER_CLASS6_IDENTIFIER = 6;
    static final int DRAWER_CLASS7_IDENTIFIER = 7;
    static final int DRAWER_CLASS8_IDENTIFIER = 8;
    static final int DRAWER_CLASS9_IDENTIFIER = 9;
    static final int DRAWER_GYMNASIUM_IDENTIFIER = 10;
    static final int DRAWER_COMPUND_INTEREST_IDENTIFIER = 11;
    static final int DRAWER_QUADRATIC_IDENTIFIER = 12;
    static final int DRAWER_REPORT_IDENTIFIER = 13;
    static final int DRAWER_ABOUT_IDENTIFIER = 14;

    // various UI elements
    ContentLoader loader = null;
    Drawer drawer = null;
    MiniDrawer miniDrawer = null;
    Crossfader crossFader = null;
    // handler to topics


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mata);

        onCreateToolbar();
        onCreateActionbar();
        onCreateDrawer(savedInstanceState);

        // kick off contentloader
        if (Matemaatika.classTitles == null) {
            showDialog(LOAD_DIALOG);
            loader = new ContentLoader(this, getAssets());
            loader.execute();
        }
    }

    void onCreateToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
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
                .withToolbar(toolbar)
                .withTranslucentStatusBar(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Lemmikud").withIcon(GoogleMaterial.Icon.gmd_star).withIdentifier(DRAWER_FAVOURITES_IDENTIFIER),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("1. klass").withIcon(GoogleMaterial.Icon.gmd_filter_1).withIdentifier(DRAWER_CLASS1_IDENTIFIER),
                        new SecondaryDrawerItem().withName("2. klass").withIcon(GoogleMaterial.Icon.gmd_filter_2).withIdentifier(DRAWER_CLASS2_IDENTIFIER),
                        new SecondaryDrawerItem().withName("3. klass").withIcon(GoogleMaterial.Icon.gmd_filter_3).withIdentifier(DRAWER_CLASS3_IDENTIFIER),
                        new SecondaryDrawerItem().withName("4. klass").withIcon(GoogleMaterial.Icon.gmd_filter_4).withIdentifier(DRAWER_CLASS4_IDENTIFIER),
                        new SecondaryDrawerItem().withName("5. klass").withIcon(GoogleMaterial.Icon.gmd_filter_5).withIdentifier(DRAWER_CLASS5_IDENTIFIER),
                        new SecondaryDrawerItem().withName("6. klass").withIcon(GoogleMaterial.Icon.gmd_filter_6).withIdentifier(DRAWER_CLASS6_IDENTIFIER),
                        new PrimaryDrawerItem().withName("7. klass").withIcon(GoogleMaterial.Icon.gmd_filter_7).withIdentifier(DRAWER_CLASS7_IDENTIFIER),
                        new PrimaryDrawerItem().withName("8. klass").withIcon(GoogleMaterial.Icon.gmd_filter_8).withIdentifier(DRAWER_CLASS8_IDENTIFIER),
                        new PrimaryDrawerItem().withName("9. klass").withIcon(GoogleMaterial.Icon.gmd_filter_9).withIdentifier(DRAWER_CLASS9_IDENTIFIER),
                        new PrimaryDrawerItem().withName("Gümnaasium").withIcon(GoogleMaterial.Icon.gmd_school).withIdentifier(DRAWER_GYMNASIUM_IDENTIFIER),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Liitintresside kalkulaator").withIcon(GoogleMaterial.Icon.gmd_trending_up).withIdentifier(DRAWER_COMPUND_INTEREST_IDENTIFIER), // need better icon here
                        new SecondaryDrawerItem().withName("Ruutvõrrandi lahendaja").withIcon(GoogleMaterial.Icon.gmd_swap_calls).withIdentifier(DRAWER_QUADRATIC_IDENTIFIER), // need better icon here
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Teavita veast").withIcon(GoogleMaterial.Icon.gmd_notifications_active).withIdentifier(DRAWER_REPORT_IDENTIFIER),
                        new PrimaryDrawerItem().withName("Programmist").withIcon(GoogleMaterial.Icon.gmd_pets).withIdentifier(DRAWER_ABOUT_IDENTIFIER)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(MataActivity.this, ((Nameable) drawerItem).getName().getText(MataActivity.this), Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                })
                .withGenerateMiniDrawer(true)
                .withSavedInstance(savedInstanceState)
                // build only the view of the Drawer (don't inflate it automatically in our layout which is done with .build())
                .buildView();

        //the MiniDrawer is managed by the Drawer and we just get it to hook it into the Crossfader
        miniDrawer = drawer.getMiniDrawer();

        //get the widths in px for the first and second panel
        int firstWidth = (int) UIUtils.convertDpToPixel(300, this);
        int secondWidth = (int) UIUtils.convertDpToPixel(72, this);

        //create and build our crossfader (see the MiniDrawer is also builded in here, as the build method returns the view to be used in the crossfader)
        //the crossfader library can be found here: https://github.com/mikepenz/Crossfader
        crossFader = new Crossfader()
                .withContent(findViewById(R.id.main_container))
                .withFirst(drawer.getSlider(), firstWidth)
                .withSecond(miniDrawer.build(this), secondWidth)
                .withSavedInstance(savedInstanceState)
                .build();

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniDrawer.withCrossFader(new CrossfadeWrapper(crossFader));

        //define a shadow (this is only for normal LTR layouts if you have a RTL app you need to define the other one
        crossFader.getCrossFadeSlidingPaneLayout().setShadowResourceLeft(R.drawable.material_drawer_shadow_left);
    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem.getIdentifier() == DRAWER_GYMNASIUM_IDENTIFIER) {

            }
            Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
        }
    };

    // this method will be called from another thread
    public void contentLoaded() {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                removeDialog(MataActivity.LOAD_DIALOG);
                Toast.makeText(getApplicationContext(), "content loaded", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarmenu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();

        // Configure the search info and add any event listeners...

        return true;
    }

}

