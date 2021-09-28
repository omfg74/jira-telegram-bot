package com.omfgdevelop.jiratelegrambot.repository

import com.omfgdevelop.jiratelegrambot.RepositorySpecBase
import com.omfgdevelop.jiratelegrambot.entity.User
import org.springframework.beans.factory.annotation.Autowired

class UserRepositoryTest extends RepositorySpecBase{


    @Autowired
    UserRepository userRepository

    def 'can insert and read user data'(){

        given:
        User user = new User()
        user.setTelegramId(123)
        user.setJiraUsername("userName")
        when:
        userRepository.save(user)

        then:
        userRepository.findByTelegramId(123).get()!=null
    }

}
