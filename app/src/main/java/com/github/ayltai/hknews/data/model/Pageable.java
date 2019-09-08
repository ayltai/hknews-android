package com.github.ayltai.hknews.data.model;

import lombok.Getter;

public final class Pageable {
    @Getter
    private Sort sort;

    @Getter
    private int offset;

    @Getter
    private int pageSize;

    @Getter
    private int pageNumber;

    @Getter
    private boolean paged;

    @Getter
    private boolean unpaged;
}
