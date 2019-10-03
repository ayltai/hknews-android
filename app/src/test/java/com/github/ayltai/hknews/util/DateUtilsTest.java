package com.github.ayltai.hknews.util;

import java.util.Date;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Test;

import com.github.ayltai.hknews.UnitTest;

public final class DateUtilsTest extends UnitTest {
    @Test
    public void testJustNow() {
        Assert.assertEquals("Just now", DateUtils.getHumanReadableDate(ApplicationProvider.getApplicationContext(), new Date(System.currentTimeMillis() - 50000L)));
    }

    @Test
    public void testOneMinuteAgo() {
        Assert.assertEquals("A minute ago", DateUtils.getHumanReadableDate(ApplicationProvider.getApplicationContext(), new Date(System.currentTimeMillis() - 110000L)));
    }

    @Test
    public void testTwentyMinutesAgo() {
        Assert.assertEquals("20 minutes ago", DateUtils.getHumanReadableDate(ApplicationProvider.getApplicationContext(), new Date(System.currentTimeMillis() - 1200000L)));
    }

    @Test
    public void testOneHourAgo() {
        Assert.assertEquals("An hour ago", DateUtils.getHumanReadableDate(ApplicationProvider.getApplicationContext(), new Date(System.currentTimeMillis() - 5000000L)));
    }

    @Test
    public void testThreeHoursAgo() {
        Assert.assertEquals("3 hours ago", DateUtils.getHumanReadableDate(ApplicationProvider.getApplicationContext(), new Date(System.currentTimeMillis() - 10800000L)));
    }

    @Test
    public void testYesterday() {
        Assert.assertEquals("Yesterday", DateUtils.getHumanReadableDate(ApplicationProvider.getApplicationContext(), new Date(System.currentTimeMillis() - 170000000L)));
    }

    @Test
    public void testFourDaysAgo() {
        Assert.assertEquals("4 days ago", DateUtils.getHumanReadableDate(ApplicationProvider.getApplicationContext(), new Date(System.currentTimeMillis() - 345600000L)));
    }

    @Test
    public void testOneMonthAgo() {
        Assert.assertEquals("A month ago", DateUtils.getHumanReadableDate(ApplicationProvider.getApplicationContext(), new Date(System.currentTimeMillis() - 3456000000L)));
    }

    @Test
    public void testNineMonthsAgo() {
        Assert.assertEquals("9 months ago", DateUtils.getHumanReadableDate(ApplicationProvider.getApplicationContext(), new Date(System.currentTimeMillis() - 23328000000L)));
    }

    @Test
    public void testOneYearAgo() {
        Assert.assertEquals("A year ago", DateUtils.getHumanReadableDate(ApplicationProvider.getApplicationContext(), new Date(System.currentTimeMillis() - 43200000000L)));
    }

    @Test
    public void testTwoYearsAgo() {
        Assert.assertEquals("2 years ago", DateUtils.getHumanReadableDate(ApplicationProvider.getApplicationContext(), new Date(System.currentTimeMillis() - 63072000000L)));
    }
}
