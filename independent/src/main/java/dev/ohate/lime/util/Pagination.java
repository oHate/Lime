package dev.ohate.lime.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Getter @Setter @RequiredArgsConstructor
public class Pagination<T> {

    public static final int PAGE_COUNT = 10;

    private final int itemsPerPage;
    private final T[] items;
    private int currentPage;

    public T[] getPageItems() {
        return Arrays.copyOfRange(this.items,
                this.currentPage * this.itemsPerPage,
                (this.currentPage + 1) * this.itemsPerPage);
    }

    public int getPageCount() {
        return (int) Math.ceil((double) this.items.length / this.itemsPerPage);
    }

    public boolean isFirst() {
        return this.currentPage == 0;
    }

    public boolean isLast() {
        return this.currentPage == getPageCount() - 1;
    }

    public boolean nextPage() {
        if (isLast()) {
            return false;
        } else {
            this.currentPage++;
            return true;
        }
    }

    public boolean previousPage() {
        if (isFirst()) {
            return false;
        } else {
            this.currentPage--;
            return true;
        }
    }

}