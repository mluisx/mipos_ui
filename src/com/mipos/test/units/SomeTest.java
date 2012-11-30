package com.mipos.test.units;

import com.mipos.activities.StockActivity;

import junit.framework.Assert;
import android.test.AndroidTestCase;


public class SomeTest extends AndroidTestCase {

    public void testSomething() throws Throwable {
//    	StockActivity stockActivity = new StockActivity();
//    	stockActivity.onCreate(null);
    	Assert.assertTrue(true);
    }

    public void testSomethingElse() throws Throwable {
       Assert.assertFalse(1 + 1 == 3);
    }
}