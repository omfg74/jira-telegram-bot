package com.omfgdevelop.jiratelegrambot.service

import com.omfgdevelop.jiratelegrambot.RepositorySpecBase
import com.omfgdevelop.jiratelegrambot.entity.User
import com.omfgdevelop.jiratelegrambot.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired

class UserServiceTest extends RepositorySpecBase{

    @Autowired
    UserService userService

    @Autowired
    UserRepository userRepository

    def 'can create user with username and telegram_id'(){

        given:
        User user = new User()
        user.setJiraUsername("test_jira_dummy_user_name")
        user.setTelegramId(234)

        when:
        userService.createOrUpdate(user)

        then:
        User userFromDb = userRepository.findByTelegramId(234).get()
        userFromDb.getJiraUsername() == user.getJiraUsername()
        userFromDb.getTelegramId() == user.getTelegramId()
    }

    def 'can add password to existing user'(){

        given:
        User user = new User()
        user.setJiraPassword("pwd")
        user.setTelegramId(3333)

        when:
        userService.createOrUpdate(user)

        then:
        User userFromDb = userRepository.findByTelegramId(3333).get()
        userFromDb.getJiraPassword()!=null
    }

}
