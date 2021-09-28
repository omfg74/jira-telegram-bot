package com.omfgdevelop.jiratelegrambot.handlers

import com.omfgdevelop.jiratelegrambot.RepositorySpecBase
import com.omfgdevelop.jiratelegrambot.entity.User
import com.omfgdevelop.jiratelegrambot.repository.UserRepository
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

import static com.omfgdevelop.jiratelegrambot.Commons.readFileToString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class NewRegistrationRequestTest extends RepositorySpecBase {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    @Autowired
    UserRepository userRepository

    def 'can register new user'() {

        given:
        def filename = "base_request_1.json"
        def filenameUserName = "base_request_2.json"
        def jsonString = readFileToString(new File(JSONS_PATH).getAbsolutePath() + "/" + filename)
        def jsonStringUserName = readFileToString(new File(JSONS_PATH).getAbsolutePath() + "/" + filenameUserName)
        def username = "test_username"

        when:
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())

        def resultUserNameInput = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStringUserName)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())


        then:
        def body = resultUserNameInput.andReturn().response.getContentAsString()
        def sendMessage = objectMapper.readValue(body, SendMessage.class)
        sendMessage.getChatId() == "1000"
        def text = String.format("User %s username added. Enter Jira password. It will be stored safely", username)
        sendMessage.getText() == text
        Optional<User> user = userRepository.findByTelegramId(1111)
        user.isPresent()

    }

    def 'can register new user and set password'() {

        given:
        def filename = "base_request_set_password_0.json"
        def filenameUserName = "base_request_set_password_1.json"
        def filenamePassword = "base_request_set_password_2.json"
        def jsonString = readFileToString(new File(JSONS_PATH).getAbsolutePath() + "/" + filename)
        def jsonStringUserName = readFileToString(new File(JSONS_PATH).getAbsolutePath() + "/" + filenameUserName)
        def jsonStringPassword = readFileToString(new File(JSONS_PATH).getAbsolutePath() + "/" + filenamePassword)
        def usernamePassword = "Aleksandr"

        when:
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())

        def resultUserNameInput = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStringUserName)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())

        def resultUserPasswordInput = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStringPassword)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())


        then:
        resultUserNameInput.andExpect(status().isOk())
        resultUserPasswordInput.andExpect(status().isOk())
        def body = resultUserNameInput.andReturn().response.getContentAsString()
        def sendMessage = objectMapper.readValue(body, SendMessage.class)
        sendMessage.getChatId() == "1000"
        def text = String.format("User %s username added. Enter Jira password. It will be stored safely", usernamePassword)
        sendMessage.getText() == text
        Optional<User> user = userRepository.findByTelegramId(1112)
        user.isPresent()
        def bodyPassword = resultUserPasswordInput.andReturn().response.getContentAsString()
        def sendMessagePassword = objectMapper.readValue(bodyPassword, SendMessage.class)
        sendMessagePassword.getChatId() == "1000"
        def textPassword = String.format("Password set to user %s.", usernamePassword)
        sendMessagePassword.getText() == textPassword
        Optional<User> userPas = userRepository.findByTelegramId(1112)
        userPas.isPresent()

        userPas.get().getTelegramId() == 1112
        userPas.get().setJiraUsername(usernamePassword)
        userPas.get().getJiraPassword() != null

    }

}
