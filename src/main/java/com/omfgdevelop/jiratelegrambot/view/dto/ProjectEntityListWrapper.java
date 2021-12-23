package com.omfgdevelop.jiratelegrambot.view.dto;

import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProjectEntityListWrapper {

    private ArrayList<ProjectEntity> projects = new ArrayList<>();

    public void add(ProjectEntity entity) {
        this.projects.add(entity);
    }

    public void addAll(List<ProjectEntity> all) {
        this.projects.addAll(all);
    }

}
