package com.omfgdevelop.jiratelegrambot.dto;

import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ProjectEntityListWrapper {

    private ArrayList<ProjectEntity> projects = new ArrayList<>();

    public void add(ProjectEntity entity) {
        this.projects.add(entity);
    }
}
