package com.example.demo.util;

import com.example.demo.dto.response.PageResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public final class PageUtil {

    private PageUtil() {
    }

    public static <T> PageResponse<T> fromPage(Page<T> page) {
        List<T> content = page.getContent();
        return PageResponse.<T>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
