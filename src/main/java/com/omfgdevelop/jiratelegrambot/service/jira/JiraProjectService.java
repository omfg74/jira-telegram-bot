package com.omfgdevelop.jiratelegrambot.service.jira;

import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
import com.omfgdevelop.jiratelegrambot.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JiraProjectService {

    private final ProjectRepository projectRepository;

    public List<ProjectEntity> getPermittedProjects() {
        return projectRepository.findAllWhereDisplayIsTrue();
    }

    @Transactional
    public void updateProjectDisplay(List<ProjectEntity> projects) {
        for (ProjectEntity project : projects) {
            projectRepository.updateDisplayProject(project.getKey(), project.getDisplay() != null ? project.getDisplay() : false);
        }
    }

    public List<ProjectEntity> getAllProjects() {
        return projectRepository.findAll();
    }
}
