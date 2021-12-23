package com.omfgdevelop.jiratelegrambot.view.dto;

import com.omfgdevelop.jiratelegrambot.entity.GroupChat;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectToChat {

//    private String projectKey;
//
//    private Integer chatId;

    private List<GroupChat> chats;
}
