package com.omfgdevelop.jiratelegrambot.controller;

import com.omfgdevelop.jiratelegrambot.view.dto.ChatListWrapper;
import com.omfgdevelop.jiratelegrambot.view.dto.ProjectEntityListWrapper;
import com.omfgdevelop.jiratelegrambot.view.dto.ProjectToChat;
import com.omfgdevelop.jiratelegrambot.entity.GroupChat;
import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import com.omfgdevelop.jiratelegrambot.service.GroupChatService;
import com.omfgdevelop.jiratelegrambot.service.jira.JiraProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class GroupChatController {

    private final GroupChatService groupChatService;

    private final JiraProjectService jiraProjectService;

    private static final String MESSAGE_ATTR = "message";

    @PostMapping("/admin/update_chats")
    public String updateChats(@ModelAttribute("chats") ChatListWrapper wrapper, RedirectAttributes redirectAttributes, Model model) {

        try {
            for (GroupChat chat : wrapper.getChats()) {
                GroupChat groupChat = groupChatService.getRegisteredChatById(chat.getId());
                if (groupChat != null) {
                    if (chat.getProject().getId().equals(""))
                        chat.getProject().setId(null);
                    groupChatService.updateActiveAndProjectLink(chat.getId(), chat.getActive(), chat.getProject().getId());
                }

            }
            redirectAttributes.addFlashAttribute(MESSAGE_ATTR, "Обновлено!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(MESSAGE_ATTR, "Error: " + e.getMessage());
            log.error(() -> new EcsEvent("Error while updating groupchats").with(e));
        }
        return "redirect:group_chats";
    }

    @PostMapping("/admin/project_to_chat")
    public String setProjectToChat(@ModelAttribute("project_to_chat") ProjectToChat projectToChat, RedirectAttributes redirectAttributes, Model model) {


        List<ProjectEntity> projectEntities = jiraProjectService.getAllProjects();
        ProjectEntityListWrapper baseListWrapper = new ProjectEntityListWrapper();
        baseListWrapper.addAll(projectEntities);
        model.addAttribute("projects", baseListWrapper);
        model.addAttribute("project_to_chat", projectToChat);

        return "project_to_chat";
    }


}
