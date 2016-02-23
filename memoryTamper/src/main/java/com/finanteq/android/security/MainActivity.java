package com.finanteq.android.security;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

/**
 * Copyright (C) 2015-2016 Finanteq.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class MainActivity extends AppCompatActivity {

    private static final int MAX_STATISTIC_PROGRESS = 10;

    private Random random = new Random(System.currentTimeMillis());

    private long lifeCounter = 0;
    private TamperSecured manaCounter = new TamperSecured(0);

    private TextView manaCounterView;
    private TextView lifeCounterView;

    private void increaseLife() {
        long newLifeValue = lifeCounter +random.nextInt(MAX_STATISTIC_PROGRESS);
        lifeCounter = newLifeValue;
        lifeCounterView.setText("Life: " + lifeCounter);
    }

    private void increaseMana() {
        long newManaValue = manaCounter.getValue() + random.nextInt(MAX_STATISTIC_PROGRESS);
        manaCounter.setValue(newManaValue);
        manaCounterView.setText("Mana: " + manaCounter.getValue());
    }

    private FloatingActionButton fabLife;

    private FloatingActionButton fabMana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setUpViews();
    }

    private void findViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fabLife = (FloatingActionButton) findViewById(R.id.fab_life);
        fabMana = (FloatingActionButton) findViewById(R.id.fab_mana);

        manaCounterView = (TextView) findViewById(R.id.mana_counter);
        lifeCounterView = (TextView) findViewById(R.id.life_counter);
    }

    private void setUpViews() {
        fabLife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseLife();
            }
        });

        fabMana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseMana();

            }
        });
    }
}
