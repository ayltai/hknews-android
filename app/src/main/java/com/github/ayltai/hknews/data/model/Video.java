package com.github.ayltai.hknews.data.model;

import io.realm.RealmObject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Video extends RealmObject {
    @Getter
    private String videoUrl;

    @Getter
    private String imageUrl;
}
