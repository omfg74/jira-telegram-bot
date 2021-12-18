package com.omfgdevelop.jiratelegrambot.controller;

import com.omfgdevelop.jiratelegrambot.dto.ProjectEntityListWrapper;
import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
import com.omfgdevelop.jiratelegrambot.service.jira.JiraProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final JiraProjectService jiraProjectService;

    @PostMapping("/display_project")
    public String setProjectDisplay(@ModelAttribute(value="wrapper") ProjectEntityListWrapper entity) {

        jiraProjectService.updateProjectDisplay(entity.getProjects());
        return "redirect:admin";
    }
}
