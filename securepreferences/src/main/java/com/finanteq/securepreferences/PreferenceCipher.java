package com.finanteq.securepreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.security.SecureRandom;
import java.util.Random;

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
public class PreferenceCipher
{
    private static final String SALT_TAG = "wsdfsdf";

    private static final int SALT_LENGTH = 32;

    private javax.crypto.Cipher encryptCipher;

    private javax.crypto.Cipher decryptCipher;

    private Context context;

    private PackageInfo applicationInfo;

    public PreferenceCipher(Context context) {
        this.context = context;
        try {
            applicationInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public String encode(String valueToEncode) {
        initIfNeeded();
        try {
            return new String(encryptCipher.doFinal(Base64.encode(valueToEncode.getBytes(), Base64.DEFAULT)));
        } catch (Exception e) {
            return null;
        }
    }

    public String decode(String valueToDecode) {
        initIfNeeded();
        try {
            return new String(encryptCipher.doFinal(Base64.decode(valueToDecode.getBytes(), Base64.DEFAULT)));
        } catch (Exception e) {
            return null;
        }
    }

    private void initIfNeeded()
    {
        if (encryptCipher != null && decryptCipher != null) {
            return;
        }
        try {

            byte[] keyBytes = deriveKey(getKeyBase(), getOrCreateSalt(context), 1500,
                    32);

            IvParameterSpec param = new IvParameterSpec(generateIv());

            SecretKeySpec key = new SecretKeySpec(keyBytes, new String(Base64.decode(new StringBuilder("TVUQ").reverse().toString(),
                    Base64.DEFAULT)));
            encryptCipher = javax.crypto.Cipher.getInstance(new String(Base64.decode(new StringBuilder("=cmbpRGZhB1NTN0SQ9yQCN0LTVUQ").reverse()
                    .toString(), Base64.DEFAULT)));
            decryptCipher = javax.crypto.Cipher.getInstance(new String(Base64.decode(new StringBuilder("=cmbpRGZhB1NTN0SQ9yQCN0LTVUQ").reverse()
                    .toString(), Base64.DEFAULT)));

            encryptCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key, param);
            decryptCipher.init(javax.crypto.Cipher.DECRYPT_MODE, key, param);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

    private
    @NonNull
    byte[] getOrCreateSalt(Context context)
    {
        SharedPreferences saltPreferences = context.getSharedPreferences(SALT_TAG, Context.MODE_PRIVATE);

        if (!saltPreferences.contains(SALT_TAG))
        {
            createAndSaveSalt(saltPreferences);
        }
        // sól zapisywana jest jako base64 z wartosci soli ktora jest odwrócona
        return Base64.decode(new StringBuilder(saltPreferences.getString(SALT_TAG, "")).reverse().toString(), Base64.NO_WRAP);
    }


    private void createAndSaveSalt(SharedPreferences saltPreferences)
    {
        // W celu zapewnienia trudności odczytu generujemy losową sól o dlugosci 8 znaków
        Random secureRandom = new SecureRandom();
        byte[] generateSalt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(generateSalt);
        saltPreferences.edit().putString(SALT_TAG, new StringBuilder(Base64.encodeToString(generateSalt, Base64.NO_WRAP)).reverse()
                .toString()).commit();
    }


    public byte[] getKeyBase() {
        return String.format("%s%s", getFirstInstallTime(), getAndroidId()).getBytes();
    }

    private byte[] generateIv() {
        return Long.toHexString(getFirstInstallTime()).getBytes();
    }

    private String getAndroidId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private long getFirstInstallTime() {
        try {
            return applicationInfo.firstInstallTime;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static byte[] deriveKey(byte[] password, byte[] salt, int iterationCount, int keyLength) throws Exception {
        byte[] key = new byte[keyLength];
        int blockCount = (int) Math.ceil((double) keyLength / 20.0D);
        int lastBlockSize = keyLength - (blockCount - 1) * 20;
        int initLen = salt.length + 4;
        byte[] init = new byte[initLen];

        for (int block = 1; block <= blockCount; ++block) {
            int idx;
            for (idx = 0; idx < salt.length; ++idx) {
                init[idx] = salt[idx];
            }

            init[salt.length] = (byte) (block >> 24);
            init[salt.length + 1] = (byte) (block >> 16);
            init[salt.length + 2] = (byte) (block >> 8);
            init[salt.length + 3] = (byte) block;
            byte[] u = hmac(password, init);
            byte[] f = u;

            for (idx = 1; idx < iterationCount; ++idx) {
                u = hmac(password, u);
                xor(f, u, f);
            }

            idx = (block - 1) * 20;
            if (block == blockCount) {
                System.arraycopy(f, 0, key, idx, lastBlockSize);
            } else {
                System.arraycopy(f, idx, key, 0, key.length);
            }
        }

        return key;
    }

    public static byte[] hmac(byte[] key, byte[] data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
        mac.init(secretKey);
        return  mac.doFinal(data);
    }


    public static void xor(byte[] buff1, byte[] buff2, byte[] dest) {
        int len = dest.length;

        for (int i = 0; i < len; ++i) {
            dest[i] = (byte) (buff1[i] ^ buff2[i]);
        }

    }
}
