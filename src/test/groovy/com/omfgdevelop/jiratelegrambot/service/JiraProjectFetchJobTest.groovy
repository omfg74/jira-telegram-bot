package com.omfgdevelop.jiratelegrambot.service

import com.omfgdevelop.jiratelegrambot.RepositorySpecBase
import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity
import com.omfgdevelop.jiratelegrambot.repository.ProjectRepository
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Ignore

class JiraProjectFetchJobTest extends RepositorySpecBase {

    @Autowired
    JiraProjectFetchJob jiraProjectService

    @Autowired
    ProjectRepository projectRepository

    @Ignore
    def 'can download projects list'() {
        given:

        when:
        jiraProjectService.getJiraProfiles('jira_admin', 'admin')
        List<ProjectEntity> list = projectRepository.findAll()

        then:
        list.size() > 0
    }
}
