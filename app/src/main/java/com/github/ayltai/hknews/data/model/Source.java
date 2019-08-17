package com.github.ayltai.hknews.data.model;

import java.util.Map;

import javax.annotation.Nonnull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode(
    callSuper              = true,
    onlyExplicitlyIncluded = true
)
@NoArgsConstructor
public class Source extends RealmObject implements Comparable<Source> {
    public static final String FIELD_NAME = "name";

    private static final Map<String, Integer> SOURCES       = new ArrayMap<>();
    private static final Map<String, String>  DISPLAY_NAMES = new ArrayMap<>();

    static {
        int i = 0;

        Source.SOURCES.put("蘋果", i++);
        Source.SOURCES.put("東方", i++);
        Source.SOURCES.put("星島", i++);
        Source.SOURCES.put("經濟", i++);
        Source.SOURCES.put("成報", i++);
        Source.SOURCES.put("明報", i++);
        Source.SOURCES.put("頭條", i++);
        Source.SOURCES.put("晴報", i++);
        Source.SOURCES.put("信報", i++);
        Source.SOURCES.put("香港", i++);
        Source.SOURCES.put("南華", i++);
        Source.SOURCES.put("英文", i++);
        Source.SOURCES.put("文匯", i);

        Source.DISPLAY_NAMES.put("蘋果日報", "蘋果日報");
        Source.DISPLAY_NAMES.put("東方日報", "東方日報");
        Source.DISPLAY_NAMES.put("星島日報", "星島日報");
        Source.DISPLAY_NAMES.put("星島即時", "星島日報");
        Source.DISPLAY_NAMES.put("經濟日報", "經濟日報");
        Source.DISPLAY_NAMES.put("成報", "成報");
        Source.DISPLAY_NAMES.put("明報", "明報");
        Source.DISPLAY_NAMES.put("頭條日報", "頭條日報");
        Source.DISPLAY_NAMES.put("頭條即時", "頭條日報");
        Source.DISPLAY_NAMES.put("晴報", "晴報");
        Source.DISPLAY_NAMES.put("信報", "信報");
        Source.DISPLAY_NAMES.put("香港電台", "香港電台");
        Source.DISPLAY_NAMES.put("南華早報", "南華早報");
        Source.DISPLAY_NAMES.put("英文虎報", "英文虎報");
        Source.DISPLAY_NAMES.put("文匯報", "文匯報");
    }

    @EqualsAndHashCode.Include
    @Getter
    @PrimaryKey
    private String name;

    @Getter
    private String imageUrl;

    @Getter
    private RealmList<Category> categories;

    @Nonnull
    @NonNull
    public static String getDisplayName(@Nonnull @NonNull @lombok.NonNull final String sourceName) {
        return Source.DISPLAY_NAMES.get(sourceName);
    }

    @Nonnull
    @NonNull
    public String getDisplayName() {
        return Source.DISPLAY_NAMES.get(this.name);
    }

    @Override
    public int compareTo(@Nullable final Source source) {
        if (source == null) throw new NullPointerException();

        return Source.SOURCES.get(this.name.substring(0, 2)) - Source.SOURCES.get(source.name.substring(0, 2));
    }
}
