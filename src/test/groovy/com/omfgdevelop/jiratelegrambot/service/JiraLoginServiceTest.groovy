package com.omfgdevelop.jiratelegrambot.service

import com.omfgdevelop.jiratelegrambot.RepositorySpecBase
import com.omfgdevelop.jiratelegrambot.entity.User
import com.omfgdevelop.jiratelegrambot.service.jira.JiraLoginService
import com.omfgdevelop.jiratelegrambot.view.jira.auth.Myself
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Ignore

class JiraLoginServiceTest extends RepositorySpecBase {

    @Autowired
    JiraLoginService jiraLoginService;

    @Autowired
    UserService userService

    @Ignore
    def 'can login to jira and collect session token and get myself data form jira'() {

        given:

        when:
        Myself myself = jiraLoginService.getMyself(4446, "123")

        then:
        User user = userService.getUserByTelegramId(4446)
        user != null
        myself.getActive()
        myself.getDisplayName() == "test user"
        myself.getEmailAddress() == "test@test.com"
        myself.getName() == "testuser"
    }
}
