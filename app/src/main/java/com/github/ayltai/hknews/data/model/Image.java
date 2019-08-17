package com.github.ayltai.hknews.data.model;

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
public class Image extends RealmObject {
    @EqualsAndHashCode.Include
    @Getter
    private String imageUrl;

    @Getter
    private String description;
}
