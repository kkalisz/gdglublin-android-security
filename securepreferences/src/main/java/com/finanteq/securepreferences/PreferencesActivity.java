package com.finanteq.securepreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Copyright (C) 2015-2016 Finanteq.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class PreferencesActivity extends AppCompatActivity {

    private static final String PREFERENCE_NAME = "PREFERENCE_NAME";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = new SecurePreferences(getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE),this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Save sample preferences", Snackbar.LENGTH_LONG)
                        .setAction("Save", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                saveSamplePreferences();
                            }
                        }).show();
            }
        });
    }

    private void saveSamplePreferences() {
        sharedPreferences.edit().putString("TEST_STRING", "TEST_VALUE").putInt("TEST_INT", 3)
                .putLong("TEST_LONG", 12837L).putBoolean("TEST_BOOLEAN", false).commit();
    }

}
