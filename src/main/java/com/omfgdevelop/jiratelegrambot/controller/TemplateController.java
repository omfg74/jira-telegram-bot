package com.omfgdevelop.jiratelegrambot.controller;

import com.omfgdevelop.jiratelegrambot.dto.ProjectEntityListWrapper;
import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
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

    @GetMapping("/admin")
    public String getCoursesView(Model model) {
        List<ProjectEntity> projects = jiraProjectService.getAllProjects();
        ProjectEntityListWrapper wrapper = new ProjectEntityListWrapper();
        for (ProjectEntity project : projects) {
            wrapper.add(project);
        }
        model.addAttribute("wrapper", wrapper);
        return "admin";
    }


}
