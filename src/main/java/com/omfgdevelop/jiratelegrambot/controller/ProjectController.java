package com.omfgdevelop.jiratelegrambot.controller;

import com.omfgdevelop.jiratelegrambot.dto.ProjectEntityListWrapper;
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

    @PostMapping("/display_project")
    public String setProjectDisplay(@ModelAttribute(value = "wrapper") ProjectEntityListWrapper entity, RedirectAttributes redirectAttributes) {
        List<ProjectEntity> entities = new ArrayList<>();
        for (int i = 0; i < entity.getProjects().size(); i++) {
            if (entity.getProjects().get(i).isDisplay()) {
                entities.add(entity.getProjects().get(i));
            }
        }
        if (entity.getProjects().isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Нет ни одного проекта, загруженного из jira. Не пинайте пианиста он верстает как умеет.");
            return "redirect:admin";
        }
        if (entities.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Выберите хотя бы один проект. Боту тоже надо кушать.");
            return "redirect:admin";
        }
        try {
            jiraProjectService.updateProjectDisplay(entity.getProjects());
            redirectAttributes.addFlashAttribute("message", "Обновлено!");
            return "redirect:admin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Ошибка: " + e.getMessage());
            return "redirect:admin";

        }
    }
}
