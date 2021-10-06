//package com.omfgdevelop.jiratelegrambot.botapi.handlers.task;
//
//import com.omfgdevelop.jiratelegrambot.botapi.UserState;
//import com.omfgdevelop.jiratelegrambot.botapi.UserStateCache;
//import com.omfgdevelop.jiratelegrambot.botapi.handlers.MessageHandler;
//import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
//import com.omfgdevelop.jiratelegrambot.service.jira.JiraProjectService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ProjectListHandler implements MessageHandler {
//
//    private final JiraProjectService service;
//
//    private final UserStateCache userStateCache;
//
//    @Override
//    public SendMessage handleInputMessage(Message message) {
//
//        List<ProjectEntity> projectList = service.getProjects();
//        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
//        List<InlineKeyboardButton> row = new ArrayList<>();
//        for (int i = 0; i < projectList.size(); i++) {
//            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
//            inlineKeyboardButton.setText(projectList.get(i).getName());
//            inlineKeyboardButton.setCallbackData(projectList.get(i).getKey());
//            row.add(inlineKeyboardButton);
////            if (i % 3 == 0) {
////                row = new ArrayList<>();
////            }
//        }
//        keyboard.add(row);
//
//
//
//        markup.setKeyboard(keyboard);
//        userStateCache.setCurrentUserState(message.getFrom().getId(), UserState.NEW_TASK_TITLE);
//        return new SendMessage().setText("Select project to create a task").setChatId(message.getChatId()).setReplyMarkup(markup);
//    }
//
//    @Override
//    public UserState getHandlerName() {
//        return UserState.PROJECT_SELECT;
//    }
//}
