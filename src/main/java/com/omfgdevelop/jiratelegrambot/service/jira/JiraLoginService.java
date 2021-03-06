package com.omfgdevelop.jiratelegrambot.service.jira;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omfgdevelop.jiratelegrambot.entity.User;
import com.omfgdevelop.jiratelegrambot.service.UserService;
import com.omfgdevelop.jiratelegrambot.view.jira.auth.Myself;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.FailedLoginException;
import javax.ws.rs.core.NoContentException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static com.omfgdevelop.jiratelegrambot.Common.createHeaders;
import static com.omfgdevelop.jiratelegrambot.config.AppConfig.ACTIVE_SESSION;

@Service
@RequiredArgsConstructor
@Log4j2
public class JiraLoginService {

    private final UserService userService;

    private RestTemplate restTemplate;

//    private final String authUrl = "/rest/auth/1/session";

    private final String apiUrl = "/rest/api/2";

    @Value("${jira.base.url}")
    private final String baseUrl;

    @Qualifier(ACTIVE_SESSION)
    private final HashMap<Long, String> activeSessions;

    ObjectMapper objectMapper = new ObjectMapper();

    public Myself getMyself(Long telegramId, String password) throws FailedLoginException, NoContentException, JsonProcessingException {
        restTemplate = new RestTemplate();
        User user = userService.getUserByTelegramId(telegramId);
        HttpHeaders headers = createHeaders(user.getJiraUsername(), password);
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(baseUrl + apiUrl + "/myself", HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().value() == 200) {
            Myself myself = objectMapper.readValue(response.getBody(), Myself.class);
            if (myself.getName().equalsIgnoreCase(user.getJiraUsername())) {
                user.setActive(myself.getActive());
                user.setDisplayName(myself.getDisplayName());
                user.setEmailAddress(myself.getEmailAddress());
                user.setKey(myself.getKey());
                user.setSelf(myself.getSelf());
                user.setName(myself.getName());
                userService.updateUser(user);
                return myself;
            } else {
                throw new FailedLoginException();
            }
        } else if (response.getStatusCode().value() == 401) {
            throw new FailedLoginException();
        }
        throw new NoContentException("Can not fetch data with current telegram id");
    }
}
