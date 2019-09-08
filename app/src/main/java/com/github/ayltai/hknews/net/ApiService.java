package com.github.ayltai.hknews.net;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

import io.reactivex.Single;

import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.model.Page;
import com.github.ayltai.hknews.data.model.Source;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @Nonnull
    @NonNull
    @GET("sources?page=0&size=20")
    Single<Page<Source>> sources();

    @Nonnull
    @NonNull
    @GET("items/{sourceNames}/{categoryNames}/{days}?page=0&size=2000")
    Single<Page<Item>> query(@Nonnull @NonNull @Path("sourceNames") String sourceNames, @Nonnull @NonNull @Path("categoryNames") String categoryNames, @Path("days") int days);
}
