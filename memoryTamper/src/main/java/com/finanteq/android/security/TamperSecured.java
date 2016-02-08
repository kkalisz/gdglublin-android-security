package com.finanteq.android.security;

import java.util.Arrays;

/**
 * Copyright (C) 2015-2016 Finanteq.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Proof of concept for class that helps protect against memory tampering
 * @param <V>
 */
public class TamperSecured<V>
{
    private byte[] hash = new byte[0];

    private V value;

    public TamperSecured(V value) {
        setValue(value);
    }

    public TamperSecured()
    {
        this(null);
    }

    public V getValue() {
        validateValue();
        return value;
    }

    private void validateValue() {
        if (!Arrays.equals(hash, Sha256Utils.digest(value))) {
            throw new IllegalStateException("Memory tampered");
        }
    }

    public void setValue(V value) {
        validateValue();
        hash = Sha256Utils.digest(value);
        this.value = value;
    }


}
