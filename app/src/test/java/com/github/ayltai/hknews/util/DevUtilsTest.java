package com.github.ayltai.hknews.util;

import android.os.Build;

import org.junit.Assert;
import org.junit.Test;

import com.github.ayltai.hknews.UnitTest;

public final class DevUtilsTest extends UnitTest {
    @Test
    public void testIsLoggable() {
        Assert.assertFalse(DevUtils.isLoggable());
    }

    @Test
    public void testIsRunningTests() {
        Assert.assertTrue(DevUtils.isRunningTests());
    }

    @Test
    public void testIsRunningUnitTest() {
        Assert.assertTrue(DevUtils.isRunningUnitTest());
    }

    @Test
    public void testIsRunningInstrumentedTest() {
        Assert.assertFalse(DevUtils.isRunningInstrumentedTest());
    }

    @Test
    public void testNewThreadPolicy() {
        if (Build.VERSION.SDK_INT >= 26) {
            Assert.assertEquals("[StrictMode.ThreadPolicy; mask=1114172]", DevUtils.newThreadPolicy().toString());
        } else if (Build.VERSION.SDK_INT >= 23) {
            Assert.assertEquals("[StrictMode.ThreadPolicy; mask=1114140]", DevUtils.newThreadPolicy().toString());
        } else if (Build.VERSION.SDK_INT >= 19) {
            Assert.assertEquals("[StrictMode.ThreadPolicy; mask=2076]", DevUtils.newThreadPolicy().toString());
        }
    }

    @Test
    public void testNewVmPolicy() {
        if (Build.VERSION.SDK_INT >= 28) {
            Assert.assertEquals("[StrictMode.VmPolicy; mask=1073854208]", DevUtils.newVmPolicy().toString());
        } else if (Build.VERSION.SDK_INT >= 26) {
            Assert.assertEquals("[StrictMode.VmPolicy; mask=112384]", DevUtils.newVmPolicy().toString());
        } else if (Build.VERSION.SDK_INT >= 23) {
            Assert.assertEquals("[StrictMode.VmPolicy; mask=79616]", DevUtils.newVmPolicy().toString());
        } else if (Build.VERSION.SDK_INT >= 19) {
            Assert.assertEquals("[StrictMode.VmPolicy; mask=28176]", DevUtils.newVmPolicy().toString());
        }
    }
}
