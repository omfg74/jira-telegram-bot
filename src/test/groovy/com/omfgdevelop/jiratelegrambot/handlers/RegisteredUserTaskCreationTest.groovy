package com.omfgdevelop.jiratelegrambot.handlers

import com.omfgdevelop.jiratelegrambot.RepositorySpecBase
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
class RegisteredUserTaskCreationTest extends RepositorySpecBase {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    def 'can create task fo registered user'() {

        given:

        //init message
        def filename = "base_request_create_task_for_registered_user_0.json"
        def initMessage = readFileToString(new File(JSONS_PATH).getAbsolutePath() + "/" + filename)


        //new title message
        def filename1 = "base_request_create_task_for_registered_user_1.json"
        def newTitleMessageMessage = readFileToString(new File(JSONS_PATH).getAbsolutePath() + "/" + filename1)

        //new text message
        def filename2 = "base_request_create_task_for_registered_user_2.json"
        def newTextMessageMessage = readFileToString(new File(JSONS_PATH).getAbsolutePath() + "/" + filename2)

        when:

        def resultInitMessageSend = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(initMessage)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())

        def resultNewTitleMessageSend = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTitleMessageMessage)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())

        def resultNewTextMessageSendMessageSend = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTextMessageMessage)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())

        then:

        //initMessage check
        def body = resultInitMessageSend.andReturn().response.getContentAsString()
        def sendMessage = objectMapper.readValue(body, SendMessage.class)
        sendMessage.getChatId() == "1000"
        def answer0 = String.format("Ready to create task for user %s.\nEnter task title", "Registered_jira_user")
        sendMessage.getText() == answer0


        //new title message  check

        def body1 = resultNewTitleMessageSend.andReturn().response.getContentAsString()
        def sendMessage1 = objectMapper.readValue(body1, SendMessage.class)
        sendMessage1.getChatId() == "1000"
        def answer1 = String.format("Task created with title %s", "sample_title")
        sendMessage1.getText() == answer1

        //new text message  check

        def body2 = resultNewTextMessageSendMessageSend.andReturn().response.getContentAsString()
        def sendMessage2 = objectMapper.readValue(body2, SendMessage.class)
        sendMessage2.getChatId() == "1000"
        def answer2 = String.format("To task with title %s added text %s", "sample_title", "sample_text")
        sendMessage2.getText() == answer2
    }


    def 'can create task for 2 registered users at the same time'() {

        given:

        //init message
        def filename = "base_request_create_task_for_registered_user_0.json"
        def initMessage = readFileToString(new File(JSONS_PATH).getAbsolutePath() + "/" + filename)


        //new title message
        def filename1 = "base_request_create_task_for_registered_user_1.json"
        def newTitleMessageMessage = readFileToString(new File(JSONS_PATH).getAbsolutePath() + "/" + filename1)

        //new text message
        def filename2 = "base_request_create_task_for_registered_user_2.json"
        def newTextMessageMessage = readFileToString(new File(JSONS_PATH).getAbsolutePath() + "/" + filename2)

        //init message
        def filenameSecondUser = "base_request_create_task_for_registered_second_user_0.json"
        def initMessageSecondUser = readFileToString(new File(JSONS_PATH).getAbsolutePath() + "/" + filenameSecondUser)


        //new title message
        def filenameSecondUser1 = "base_request_create_task_for_registered_second_user_1.json"
        def newTitleMessageMessageSecondUser = readFileToString(new File(JSONS_PATH).getAbsolutePath() + "/" + filenameSecondUser1)

        //new text message
        def filenameSecondUser2 = "base_request_create_task_for_registered_second_user_2.json"
        def newTextMessageMessageSecondUser = readFileToString(new File(JSONS_PATH).getAbsolutePath() + "/" + filenameSecondUser2)



        when:

        def resultInitMessageSend = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(initMessage)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())

        def resultNewTitleMessageSend = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTitleMessageMessage)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())

        def resultInitMessageSendSecondUser = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(initMessageSecondUser)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())


        def resultNewTitleMessageSendSecondUser = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTitleMessageMessageSecondUser)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())

        def resultNewTextMessageSendMessageSend = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTextMessageMessage)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())

        def resultNewTextMessageSendMessageSendSecondUser = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTextMessageMessageSecondUser)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())

        then:

        //initMessage check
        def body = resultInitMessageSend.andReturn().response.getContentAsString()
        def sendMessage = objectMapper.readValue(body, SendMessage.class)
        sendMessage.getChatId() == "1000"
        def answer0 = String.format("Ready to create task for user %s.\nEnter task title", "Registered_jira_user")
        sendMessage.getText() == answer0


        //new title message  check

        def body1 = resultNewTitleMessageSend.andReturn().response.getContentAsString()
        def sendMessage1 = objectMapper.readValue(body1, SendMessage.class)
        sendMessage1.getChatId() == "1000"
        def answer1 = String.format("Task created with title %s", "sample_title")
        sendMessage1.getText() == answer1

        //new text message  check

        def body2 = resultNewTextMessageSendMessageSend.andReturn().response.getContentAsString()
        def sendMessage2 = objectMapper.readValue(body2, SendMessage.class)
        sendMessage2.getChatId() == "1000"
        def answer2 = String.format("To task with title %s added text %s", "sample_title", "sample_text")
        sendMessage2.getText() == answer2

        //Second user check

        //initMessage check
        def bodySecondUser = resultInitMessageSendSecondUser.andReturn().response.getContentAsString()
        def sendMessageSecondUser = objectMapper.readValue(bodySecondUser, SendMessage.class)
        sendMessageSecondUser.getChatId() == "1000"
        def answer0SecondUser = String.format("Ready to create task for user %s.\nEnter task title", "Registered_jira_user_SecondUser")
        sendMessageSecondUser.getText() == answer0SecondUser


        //new title message  check

        def body1SecondUser = resultNewTitleMessageSendSecondUser.andReturn().response.getContentAsString()
        def sendMessage1SecondUser = objectMapper.readValue(body1SecondUser, SendMessage.class)
        sendMessage1SecondUser.getChatId() == "1000"
        def answer1SecondUser = String.format("Task created with title %s", "sample_title_SecondUser")
        sendMessage1SecondUser.getText() == answer1SecondUser

        //new text message  check

        def body2SecondUser = resultNewTextMessageSendMessageSendSecondUser.andReturn().response.getContentAsString()
        def sendMessage2SecondUser = objectMapper.readValue(body2SecondUser, SendMessage.class)
        sendMessage2SecondUser.getChatId() == "1000"
        def answer2SecondUser = String.format("To task with title %s added text %s", "sample_title_SecondUser", "sample_text_SecondUser")
        sendMessage2SecondUser.getText() == answer2SecondUser
    }
}
