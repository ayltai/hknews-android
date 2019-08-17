package com.github.ayltai.hknews.data.model;

import java.util.Map;

import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;

import io.realm.RealmList;
import io.realm.RealmObject;

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
public class Category extends RealmObject implements Comparable<Category> {
    private static final Map<String, Integer> CATEGORIES = new ArrayMap<>();

    static {
        int i = 0;

        Category.CATEGORIES.put("港聞", i++);
        Category.CATEGORIES.put("兩岸", i++);
        Category.CATEGORIES.put("國際", i++);
        Category.CATEGORIES.put("經濟", i++);
        Category.CATEGORIES.put("地產", i++);
        Category.CATEGORIES.put("娛樂", i++);
        Category.CATEGORIES.put("體育", i++);
        Category.CATEGORIES.put("副刊", i++);
        Category.CATEGORIES.put("教育", i);
    }

    @Getter
    private RealmList<String> urls;

    @EqualsAndHashCode.Include
    @Getter
    private String name;

    @Override
    public int compareTo(@Nullable final Category category) {
        if (category == null) throw new NullPointerException();

        return Category.CATEGORIES.get(this.name) - Category.CATEGORIES.get(category.name);
    }
}
