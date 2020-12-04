package ru.ibs.gasu.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilterPagingLoadConfig implements Serializable {
    private List<SortInfo> sortInfo = new ArrayList<>();
    private List<FilterConfig> filters = new ArrayList<>();
    private int limit;
    private int offset;
    private boolean disjunction;

    public FilterPagingLoadConfig() {}

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<FilterConfig> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterConfig> filters) {
        this.filters = filters;
    }

    public List<SortInfo> getSortInfo() {
        return sortInfo;
    }

    public void setSortInfo(List<SortInfo> info) {
        sortInfo = info;
    }

    public boolean isDisjunction() {
        return disjunction;
    }

    public boolean getDisjunction() {
        return disjunction;
    }

    public void setDisjunction(boolean disjunction) {
        this.disjunction = disjunction;
    }

}
