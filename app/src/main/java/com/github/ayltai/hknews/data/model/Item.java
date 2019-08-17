package com.github.ayltai.hknews.data.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(
    callSuper              = true,
    onlyExplicitlyIncluded = true
)
@NoArgsConstructor
public class Item extends RealmObject {
    //region Constants

    public static final String FIELD_TITLE         = "title";
    public static final String FIELD_DESCRIPTION   = "description";
    public static final String FIELD_URL           = "url";
    public static final String FIELD_SOURCE        = "source.name";
    public static final String FIELD_CATEGORY      = "category.name";
    public static final String FIELD_PUBLISH_DATE  = "publishDate";
    public static final String FIELD_LAST_ACCESSED = "lastAccessed";
    public static final String FIELD_IS_BOOKMARKED = "isBookmarked";

    //endregion

    @Getter
    private String title;

    @Getter
    private String description;

    @Getter
    @PrimaryKey
    private String url;

    @Getter
    private Date publishDate;

    @Getter
    private Source source;

    @Getter
    private Category category;

    @Getter
    private RealmList<Image> images;

    @Getter
    private RealmList<Video> videos;

    @Getter
    @Setter
    private Date lastAccessed;

    @Getter
    @Setter
    private boolean isBookmarked;
}
