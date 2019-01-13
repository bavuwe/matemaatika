package com.bavuwe.matemaatika;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MataActivity extends AppCompatActivity {
    static final int LOAD_DIALOG = 0;
    private ContentLoader loader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mata);

        setTitle("Matemaatika"); // safe title to use whenever needed

        // make sure we always have content loaded
        if (Matemaatika.classTitles == null) {
            showDialog(LOAD_DIALOG);
            loader = new ContentLoader(this);
            loader.execute();
        }
    }

    /**
     * Let the activity know that we are ready with loading content.
     */
    public void contentLoaded() {
        Toast.makeText(getApplicationContext(), "content loaded", Toast.LENGTH_LONG).show();
    }
}

