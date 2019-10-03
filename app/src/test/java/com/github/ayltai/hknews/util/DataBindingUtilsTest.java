package com.github.ayltai.hknews.util;

import android.text.Html;
import android.widget.TextView;

import org.junit.Test;
import org.mockito.Mockito;

import com.github.ayltai.hknews.UnitTest;

public final class DataBindingUtilsTest extends UnitTest {
    private static final String DUMMY_STRING = "dummy";

    @Test
    public void testSetNullTitle() {
        final TextView view = Mockito.mock(TextView.class);

        DataBindingUtils.setTitle(view, null);

        Mockito.verify(view, Mockito.times(1)).setText(null);
    }

    @Test
    public void testSetDummyTitle() {
        final TextView view = Mockito.mock(TextView.class);

        DataBindingUtils.setTitle(view, DataBindingUtilsTest.DUMMY_STRING);

        Mockito.verify(view, Mockito.times(1)).setText(Html.fromHtml(DataBindingUtilsTest.DUMMY_STRING));
    }

    @Test
    public void testSetNullDescription() {
        final TextView view = Mockito.mock(TextView.class);

        DataBindingUtils.setDescription(view, null);

        Mockito.verify(view, Mockito.times(1)).setText(null);
    }

    @Test
    public void testSetDummyDescription() {
        final TextView view = Mockito.mock(TextView.class);

        DataBindingUtils.setDescription(view, DataBindingUtilsTest.DUMMY_STRING);

        Mockito.verify(view, Mockito.times(1)).setText(Html.fromHtml(DataBindingUtilsTest.DUMMY_STRING));
    }
}
