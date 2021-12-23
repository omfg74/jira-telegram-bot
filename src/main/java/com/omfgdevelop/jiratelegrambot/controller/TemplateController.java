package com.omfgdevelop.jiratelegrambot.controller;

import com.omfgdevelop.jiratelegrambot.view.dto.ChatListWrapper;
import com.omfgdevelop.jiratelegrambot.view.dto.ProjectEntityListWrapper;
import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
import com.omfgdevelop.jiratelegrambot.service.GroupChatService;
import com.omfgdevelop.jiratelegrambot.service.jira.JiraProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TemplateController {


    private final JiraProjectService jiraProjectService;

    private final GroupChatService groupChatService;

    private static final String TITLE_ATTR = "title";
    private static final String PROJECTS_ATTR = "projects";
    private static final String CHATS_ATTR = "chats";

    @GetMapping("/admin/projects")
    public String getProjectsView(Model model) {
        List<ProjectEntity> projects = jiraProjectService.getAllProjects();
        ProjectEntityListWrapper wrapper = new ProjectEntityListWrapper();
        model.addAttribute(TITLE_ATTR, "Проекты Jira");
        for (ProjectEntity project : projects) {
            wrapper.add(project);
        }
        model.addAttribute("wrapper", wrapper);
        return "projects";
    }

    @GetMapping("/admin")
    public String getAdminView(Model model) {
        model.addAttribute(TITLE_ATTR, "Админка");
        return "admin";
    }

    @GetMapping("/admin/group_chats")
    public String getGroupChatsView(Model model) {
        model.addAttribute(TITLE_ATTR, "Групповые чаты");
        ChatListWrapper chats = new ChatListWrapper();
        chats.addAll(groupChatService.getAll());
        List<ProjectEntity> projects = jiraProjectService.getAllProjects();
        model.addAttribute(PROJECTS_ATTR, projects);
        model.addAttribute(CHATS_ATTR, chats);
        return "group_chats";
    }

}
