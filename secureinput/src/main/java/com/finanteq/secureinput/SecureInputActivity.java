package com.finanteq.secureinput;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.finanteq.datatypes.SecureCharSequence;

import eu.eleader.android.finance.forms.items.SecureInputFilter;

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
public class SecureInputActivity extends AppCompatActivity {

    public static final char[] WALLY = new char[]{'W', 'h', 'e', 'r', 'e', ' ', 'I', 's', ' ', 'W', 'a', 'l', 'l', 'y', ' ', '!', '?'};

    private Beacon<String> simpleString = new Beacon<>(new String(WALLY));

    private Beacon<OwaspSecureString>  owaspSecureString = new Beacon<>(new OwaspSecureString(WALLY));

    private Beacon<SecureCharSequence> secureString = new Beacon<>(new SecureCharSequence(WALLY));


    private EditText standardInput;

    private EditText secureCharSequenceInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_input);
        setUpStandardInput();

        setUpSecureInput();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpSecureInput() {
        secureCharSequenceInput = (EditText) findViewById(R.id.secure_char_sequence_password_input);
        SecureInputFilter secureInputFilter = new SecureInputFilter();
        secureCharSequenceInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        secureCharSequenceInput.setFilters(new InputFilter[]{secureInputFilter});
        secureInputFilter.getSecureCharSequence();
    }

    private void setUpStandardInput() {
        standardInput = (EditText) findViewById(R.id.standard_password_input);
        standardInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
    }

}
