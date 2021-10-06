package com.omfgdevelop.jiratelegrambot.mapping;

import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
import com.omfgdevelop.jiratelegrambot.view.jira.prject.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = CustomMapper.class)
public interface UberMapper {
    List<ProjectEntity> map(List<Project> projects);
    ProjectEntity map(Project projects);
}
