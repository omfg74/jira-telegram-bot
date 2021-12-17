package com.omfgdevelop.jiratelegrambot.handlers

import com.omfgdevelop.jiratelegrambot.ObjectMapperConfig
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
class BaseRequestTest extends RepositorySpecBase {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    def 'can collect correct answer from NewRegistrationHandler'(String filename) {

        given:

        def jsonString = readFileToString(new File(JSONS_PATH).getAbsolutePath() + "/" + filename)

        when:
        def result = mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
                .characterEncoding("utf-8"))

        then:
        result.andExpect(status().isOk())
        def body = result.andReturn().response.getContentAsString()
        def sendMessage = objectMapper.readValue(body, SendMessage.class)
        sendMessage.getChatId()=="1000"
//        sendMessage.getText()=="Unregistered. Input your Jira user name"

        where:
        filename              | expected
        "base_request_0.json" | null
    }
}
