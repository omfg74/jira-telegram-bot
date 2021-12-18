package com.omfgdevelop.jiratelegrambot.service.jira;

import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
import com.omfgdevelop.jiratelegrambot.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JiraProjectService {

    private final ProjectRepository projectRepository;

    public List<ProjectEntity> getPermittedProjects() {
        return projectRepository.findAllWhereDisplayIsTrue();
    }

    @Transactional
    public void updateProjectDisplay(ArrayList<ProjectEntity> projects) {
        for (int i = 0; i < projects.size(); i++) {
            projectRepository.updateDisplayProject(projects.get(i).getKey(), projects.get(i).isDisplay());
        }
    }

    public List<ProjectEntity> getAllProjects() {
        return projectRepository.findAll();
    }
}
