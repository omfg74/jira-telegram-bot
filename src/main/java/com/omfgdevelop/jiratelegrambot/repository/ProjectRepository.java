package com.omfgdevelop.jiratelegrambot.repository;

import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {

}
