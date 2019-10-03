package com.github.ayltai.hknews.util;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Test;

import com.github.ayltai.hknews.UnitTest;

public final class AnimationUtilsTest extends UnitTest {
    @Test
    public void testAreAnimatorsEnabled() {
        Assert.assertTrue(AnimationUtils.areAnimatorsEnabled());
    }

    @Test
    public void testGetAnimationDuration() {
        Assert.assertEquals(400, AnimationUtils.getAnimationDuration(ApplicationProvider.getApplicationContext(), android.R.integer.config_mediumAnimTime));
    }
}
