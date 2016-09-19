package org.trams.sicbang.model.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;

/**
 * Created by voncount on 4/7/16.
 */
public abstract class BaseFormSearch<T extends Object> implements Serializable {

    protected String query;
    @ApiModelProperty(value = "Separation by comma ','")
    protected String orders;
    @ApiModelProperty(value = "ASC | DESC")
    protected String direction;
    protected int pageIndex = 0;
    @ApiModelProperty(hidden = true)
    protected int pageSize = 10;
    @ApiModelProperty(hidden = true)
    protected int isDelete = 0;
    protected String date;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Pageable getPaging() {
        if (orders == null || orders.isEmpty()) {
            return new PageRequest(this.pageIndex, this.pageSize);
        } else {
            if (direction == null || direction.trim().isEmpty()) {
                return new PageRequest(this.pageIndex, this.pageSize, Sort.Direction.ASC, orders.split(","));
            }
            return new PageRequest(this.pageIndex, this.pageSize, Sort.Direction.fromStringOrNull(direction), orders.split(","));
        }
    }

    public abstract Specification<T> getSpecification();

}
