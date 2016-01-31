package com.finanteq.android.security;

import android.support.test.runner.AndroidJUnit4;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.lang.reflect.Field;

/**
 * Copyright (C) 20015 Finanteq.
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
@RunWith(AndroidJUnit4.class)
public class TamperSecuredTest {

    @Test
    public void whenInitWithNullValueGetValueWillReturnNullValue()
    {
        TamperSecured<String> myString = new TamperSecured<>(null);

        Assert.assertNull(myString.getValue());
    }

    @Test
    public void whenInitWithLongValueGetWillReturnProperLongValue()
    {
        TamperSecured<Long> myLong = new TamperSecured<>(234l);

        Long expectedValue = 234l;
        Assert.assertEquals(expectedValue, myLong.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void whenInitWithValueAndModifyOriginalValueGetWillThrowException() throws NoSuchFieldException, IllegalAccessException {
        TamperSecured<String> myStringValue = new TamperSecured<>("Hej Hej");

        Field valueField = TamperSecured.class.getDeclaredField("value");
        valueField.setAccessible(true);

        valueField.set(myStringValue, "Hi HI");

        myStringValue.getValue();


    }

}