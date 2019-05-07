package com.github.ayltai.hknews.net;

import java.util.List;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.data.model.Item;
import com.github.ayltai.hknews.data.model.Source;

import io.reactivex.Single;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @Nonnull
    @NonNull
    @GET("sources")
    Single<List<Source>> sources();

    @Nonnull
    @NonNull
    @GET("items/{sourceNames}/{categoryNames}/{days}")
    Single<List<Item>> query(@Nonnull @NonNull @Path("sourceNames") String sourceNames, @Nonnull @NonNull @Path("categoryNames") String categoryNames, @Path("days") int days);
}
