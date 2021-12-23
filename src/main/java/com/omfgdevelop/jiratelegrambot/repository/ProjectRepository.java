package com.omfgdevelop.jiratelegrambot.repository;

import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    @Modifying
    @Query(value = "update jira_project p set display=?2 where p.key=?1 ", nativeQuery = true)
    void updateDisplayProject(String key, boolean display);


    ProjectEntity findByKey(String key);

    @Query(value = "select * from jira_project p where p.display=true", nativeQuery = true)
    List<ProjectEntity> findAllWhereDisplayIsTrue();

    Optional<ProjectEntity> findOneByKey(String key);
}
