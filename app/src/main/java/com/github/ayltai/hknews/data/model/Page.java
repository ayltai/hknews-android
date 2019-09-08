package com.github.ayltai.hknews.data.model;

import java.util.List;

import lombok.Getter;

public final class Page<T> {
    @Getter
    private List<T> content;

    @Getter
    private Pageable pageable;

    @Getter
    private Sort sort;

    @Getter
    private int size;

    @Getter
    private int number;

    @Getter
    private int numberOfElements;

    @Getter
    private int totalPages;

    @Getter
    private int totalElements;

    @Getter
    private boolean first;

    @Getter
    private boolean last;

    @Getter
    private boolean empty;
}
