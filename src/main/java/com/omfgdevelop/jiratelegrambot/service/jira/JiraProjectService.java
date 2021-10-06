package com.omfgdevelop.jiratelegrambot.service.jira;

import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
import com.omfgdevelop.jiratelegrambot.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JiraProjectService {

    private final ProjectRepository projectRepository;

    public List<ProjectEntity> getProjects() {
        return projectRepository.findAll();
    }

}
