package com.github.ayltai.hknews;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final int CONNECT_TIMEOUT = 30;
    public static final int READ_TIMEOUT    = 60;
    public static final int WRITE_TIMEOUT   = 60;

    public static final int   TFL_MODEL_WIDTH     = 176;
    public static final int   TFL_MODEL_HEIGHT    = 176;
    public static final float RELEVANCE_THRESHOLD = 0.75f;

    public static final int MIN_FETCH_TIME = 30;

    public static final float ITEM_ALPHA = 0.85f;

    public static final int CACHE_SIZE = 1000;
    public static final int MAX_ZOOM   = 8;
    public static final int NEWS_DAYS  = 3;

    public static final String BASE_URL = "https://hknews.dev/";
}
