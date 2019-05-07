package com.github.ayltai.hknews;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final int CONNECT_TIMEOUT = 30;
    public static final int READ_TIMEOUT    = 60;
    public static final int WRITE_TIMEOUT   = 60;

    public static final int NEWS_DAYS = 3;

    public static final String BASE_URL = "https://hknews.dev/";
}
