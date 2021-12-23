package com.omfgdevelop.jiratelegrambot.view.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BaseListWrapper<T> {

    private List<T> items = new ArrayList<>();

    public void add(T item) {
        this.items.add(item);
    }

    public void addAll(List<T> items) {
        this.items.addAll(items);
    }

    public void clear(){
        this.items.clear();
    }

}
