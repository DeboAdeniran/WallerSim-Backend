package com.example.walletsim_backend.dto.response;

import java.util.List;

public class PagedResponse<T> {
    private List<T> item;
    private Pagination pagination;

    public List<T> getItem() {
        return item;
    }

    public void setItem(List<T> item) {
        this.item = item;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
