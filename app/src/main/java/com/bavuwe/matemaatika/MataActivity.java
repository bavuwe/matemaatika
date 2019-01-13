package com.bavuwe.matemaatika;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.crossfader.util.UIUtils;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.ToggleDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.octicons_typeface_library.Octicons;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

public class MataActivity extends AppCompatActivity {
    static final int LOAD_DIALOG = 0;
    ContentLoader loader = null;
    Drawer drawer = null;
    MiniDrawer miniDrawer = null;
    Crossfader crossFader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mata);

        // make sure we have always materials loaded
        if (Matemaatika.classTitles == null) {
            showDialog(LOAD_DIALOG);
            loader = new ContentLoader(this);
            loader.execute();
        }

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withTranslucentStatusBar(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.app_name).withIcon(GoogleMaterial.Icon.gmd_brightness_5).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.app_name).withIcon(FontAwesome.Icon.faw_home).withBadge("22").withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.app_name).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.app_name).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(4),
                        new PrimaryDrawerItem().withDescription("A more complex sample").withName(R.string.app_name).withIcon(GoogleMaterial.Icon.gmd_adb).withIdentifier(5),
                        new SectionDrawerItem().withName(R.string.app_name),
                        new SecondaryDrawerItem().withName(R.string.app_name).withIcon(FontAwesome.Icon.faw_github),
                        new SecondaryDrawerItem().withName(R.string.app_name).withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withTag("Bullhorn"),
                        new DividerDrawerItem(),
                        new SwitchDrawerItem().withName("Switch").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        new ToggleDrawerItem().withName("Toggle").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)
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
                .withContent(findViewById(R.id.crossfade_content))
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
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };

    /**
     * Let the activity know that we are ready with loading content.
     */
    public void contentLoaded() {
        Toast.makeText(getApplicationContext(), "content loaded", Toast.LENGTH_LONG).show();
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

