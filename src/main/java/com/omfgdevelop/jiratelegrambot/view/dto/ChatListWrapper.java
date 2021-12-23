package com.omfgdevelop.jiratelegrambot.view.dto;

import com.omfgdevelop.jiratelegrambot.entity.GroupChat;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatListWrapper {

    private List<GroupChat> chats = new ArrayList<>();

    public void add(GroupChat item) {
        this.chats.add(item);
    }

    public void addAll(List<GroupChat> all) {
        this.chats.addAll(all);
    }
}
