package com.codingblocks.dynamiclink;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import android.widget.TextView;

public class deeplinkclass extends AppCompatActivity implements View.OnClickListener {

        private static final String TAG = deeplinkclass.class.getSimpleName();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit);
            findViewById(R.id.buttonOk).setOnClickListener(this);
        }

        @Override
        protected void onStart() {
            super.onStart();
            if (getIntent() != null && getIntent().getData() != null) {
                Uri data = getIntent().getData();

                Log.d(TAG, "data:" + data);
                ((TextView) findViewById(R.id.deepLinkText))
                        .setText(getString(R.string.deep_link_fmt, data.toString()));
            }
        }


        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.buttonOk) {
                finish();
            }
        }
    }