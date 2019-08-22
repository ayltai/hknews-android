package com.github.ayltai.hknews.util;

import java.util.Date;

import javax.annotation.Nonnull;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.ayltai.hknews.R;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtils {
    //region Constants

    private static final long SECOND_MILLIS = 1000;
    private static final long MINUTE_MILLIS = 60 * DateUtils.SECOND_MILLIS;
    private static final long HOUR_MILLIS   = 60 * DateUtils.MINUTE_MILLIS;
    private static final long DAY_MILLIS    = 24 * DateUtils.HOUR_MILLIS;
    private static final long DAY_DAYS      = 30;
    private static final long MONTH_DAYS    = 45;
    private static final long YEAR_DAYS     = 365;
    private static final long YEARS_DAYS    = 550;

    //endregion

    public String getHumanReadableDate(@Nonnull @NonNull @lombok.NonNull final Context context, @Nonnull @NonNull @lombok.NonNull final Date date) {
        final long time = date.getTime();
        final long now  = System.currentTimeMillis();

        if (time > now || time <= 0) return context.getString(R.string.time_ago_now);

        final long diff = now - time;

        if (diff < DateUtils.MINUTE_MILLIS) return context.getString(R.string.time_ago_now);
        if (diff < 2 * DateUtils.MINUTE_MILLIS) return context.getString(R.string.time_ago_minute);
        if (diff < 50 * DateUtils.MINUTE_MILLIS) return String.format(context.getString(R.string.time_ago_minutes), String.valueOf(Math.round(diff / (double)DateUtils.MINUTE_MILLIS)));
        if (diff < 90 * DateUtils.MINUTE_MILLIS) return context.getString(R.string.time_ago_hour);
        if (diff < 24 * DateUtils.HOUR_MILLIS) return String.format(context.getString(R.string.time_ago_hours), String.valueOf(Math.round(diff / (double)DateUtils.HOUR_MILLIS)));
        if (diff < 48 * DateUtils.HOUR_MILLIS) return context.getString(R.string.time_ago_day);

        final long days = Math.round(diff / (double)DateUtils.DAY_MILLIS);

        if (days < DateUtils.DAY_DAYS) return context.getString(R.string.time_ago_days);
        if (days < DateUtils.MONTH_DAYS) return context.getString(R.string.time_ago_month);
        if (days < DateUtils.YEAR_DAYS) return context.getString(R.string.time_ago_months);
        if (days < DateUtils.YEARS_DAYS) return context.getString(R.string.time_ago_year);

        return String.format(context.getString(R.string.time_ago_years), String.valueOf(Math.round(days / (double)DateUtils.YEAR_DAYS)));
    }
}
