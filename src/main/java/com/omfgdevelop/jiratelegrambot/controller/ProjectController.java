package com.omfgdevelop.jiratelegrambot.controller;

import com.omfgdevelop.jiratelegrambot.view.dto.ProjectEntityListWrapper;
import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
import com.omfgdevelop.jiratelegrambot.service.jira.JiraProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final JiraProjectService jiraProjectService;

    private final String ATTR_MESSAGE = "message";

    @PostMapping("/admin/display_project")
    public String setProjectDisplay(@ModelAttribute(value = "wrapper") ProjectEntityListWrapper entity, RedirectAttributes redirectAttributes) {
        List<ProjectEntity> entities = new ArrayList<>();
        for (int i = 0; i < entity.getProjects().size(); i++) {
            if (Boolean.TRUE.equals(entity.getProjects().get(i).getDisplay())) {
                entities.add(entity.getProjects().get(i));
            }
        }
        if (entity.getProjects().isEmpty()) {
            redirectAttributes.addFlashAttribute(ATTR_MESSAGE, "Нет ни одного проекта, загруженного из jira. Не пинайте пианиста он верстает как умеет.");
            return "redirect:projects";
        }
        if (entities.isEmpty()) {
            redirectAttributes.addFlashAttribute(ATTR_MESSAGE, "Выберите хотя бы один проект. Боту тоже надо кушать.");
            return "redirect:projects";
        }
        try {
            jiraProjectService.updateProjectDisplay(entity.getProjects());
            redirectAttributes.addFlashAttribute(ATTR_MESSAGE, "Обновлено!");
            return "redirect:projects";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ATTR_MESSAGE, "Ошибка: " + e.getMessage());
            return "redirect:projects";

        }
    }
}
