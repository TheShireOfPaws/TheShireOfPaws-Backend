package com.theshireofpaws.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageRequests {

    public static final int MAX_PAGE_SIZE = 50;

    private PageRequests() {
    }

    public static Pageable of(int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromOptionalString(sortDir).orElse(Sort.Direction.DESC), sortBy);
        return PageRequest.of(page, Math.min(size, MAX_PAGE_SIZE), sort);
    }

    public static Pageable newestFirst(int page, int size) {
        return of(page, size, "createdAt", "DESC");
    }
}
