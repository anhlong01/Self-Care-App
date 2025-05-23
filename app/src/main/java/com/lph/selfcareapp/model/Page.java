package com.lph.selfcareapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Page<T> {
    @SerializedName("content")
    @Expose
    private List<T> content;
    @SerializedName("last")
    @Expose
    private Boolean last;
    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("numberOfElements")
    @Expose
    private Integer numberOfElements;
    @SerializedName("first")
    @Expose
    private Boolean first;
    @SerializedName("empty")
    @Expose
    private Boolean empty;

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }


    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }
}
