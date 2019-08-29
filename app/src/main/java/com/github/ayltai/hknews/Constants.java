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

    public static final int CACHE_SIZE = 1024;
    public static final int MAX_ZOOM   = 8;

    public static final int NEWS_DAYS         = 2;
    public static final int LAST_UPDATED_TIME = 10 * 1000;
    public static final int AUTO_REFRESH_TIME = 30 * 60 * 1000;

    public static final int SEARCH_TYPE_UNDEFINED = -1;
    public static final int SEARCH_TYPE_NEWS      = 0;
    public static final int SEARCH_TYPE_BOOKMARKS = 1;
    public static final int SEARCH_TYPE_HISTORIES = 2;

    public static final String BASE_URL = "https://hknews.dev/";
}
