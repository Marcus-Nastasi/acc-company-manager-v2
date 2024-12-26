package com.accenture.test.domain.supplier;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class SupplierPag implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<Supplier> data;
    private int page;
    private int total;
    private int remainingPages;

    public SupplierPag(List<Supplier> data, int page, int total, int remainingPages) {
        this.data = data;
        this.page = page;
        this.total = total;
        this.remainingPages = remainingPages;
    }

    public List<Supplier> getData() {
        return data;
    }

    public void setData(List<Supplier> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRemainingPages() {
        return remainingPages;
    }

    public void setRemainingPages(int remainingPages) {
        this.remainingPages = remainingPages;
    }
}
