package com.finanteq.securepreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Copyright (C) 20015 Finanteq.
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
public class SecurePreferences implements SharedPreferences {

    private final Context context;

    private SharedPreferences delegate;

    private PreferenceCipher preferenceCipher;


    public SecurePreferences(SharedPreferences delegate, Context context) {
        this.delegate = delegate;
        this.context = context;
        preferenceCipher = new PreferenceCipher(context);
    }

    @Override
    public Map<String, ?> getAll() {
        throw new IllegalStateException("Not implemented yet");
    }

    @Nullable
    @Override
    public String getString(String key, String defValue) {
        return decode(delegate.getString(encode(key), encode(defValue)));
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public int getInt(String key, int defValue) {
        return Integer.valueOf(getString(key, Integer.toString(defValue)));
    }

    @Override
    public long getLong(String key, long defValue) {
        return Long.valueOf(getString(key, Long.toString(defValue)));
    }

    @Override
    public float getFloat(String key, float defValue) {
        return Float.valueOf(getString(key, Float.toString(defValue)));
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return Boolean.valueOf(getString(key, Boolean.toString(defValue)));
    }

    @Override
    public boolean contains(String key) {
        return delegate.contains(encode(key));
    }

    @Override
    public Editor edit() {
        return new Editor() {
            private Editor editorDelegate = delegate.edit();

            @Override
            public Editor putString(String key, String value) {
                editorDelegate.putString(encode(key), encode(value));
                return this;
            }

            @Override
            public Editor putStringSet(String key, Set<String> values) {
                throw new IllegalStateException("Not implemented yet");
            }

            @Override
            public Editor putInt(String key, int value) {
                editorDelegate.putString(encode(key), encode(Integer.toString(value)));
                return this;
            }

            @Override
            public Editor putLong(String key, long value) {
                editorDelegate.putString(encode(key), encode(Long.toString(value)));
                return this;
            }

            @Override
            public Editor putFloat(String key, float value) {
                editorDelegate.putString(encode(key), encode(Float.toString(value)));
                return this;
            }

            @Override
            public Editor putBoolean(String key, boolean value) {
                editorDelegate.putString(encode(key), encode(Boolean.toString(value)));
                return this;
            }

            @Override
            public Editor remove(String key) {
                editorDelegate.remove(encode(key));
                return this;
            }

            @Override
            public Editor clear() {
                editorDelegate.clear();
                return this;
            }

            @Override
            public boolean commit() {
                return editorDelegate.commit();
            }

            @Override
            public void apply() {
                editorDelegate.apply();
            }
        };
    }

    private Map<OnSharedPreferenceChangeListener, OnSharedPreferenceChangeListener> listenerAdapters = new HashMap<>();

    @Override
    public void registerOnSharedPreferenceChangeListener(final OnSharedPreferenceChangeListener listener) {
        OnSharedPreferenceChangeListener listenerAdapter = new OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                listener.onSharedPreferenceChanged(SecurePreferences.this, decode(key));
            }
        };
        listenerAdapters.put(listener, listenerAdapter);
        delegate.registerOnSharedPreferenceChangeListener(listenerAdapter);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        listenerAdapters.remove(listenerAdapters.get(listener));
    }

    public String encode(String valueToEncode) {
        return preferenceCipher.encode(valueToEncode);
    }

    public String decode(String valueToDecode) {
        return preferenceCipher.decode(valueToDecode);
    }
}


