package com.github.ayltai.hknews.widget;

import android.os.Parcelable;

import flow.ClassKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ModelKey<T> extends ClassKey implements Parcelable {
    public abstract T getModel();
}
