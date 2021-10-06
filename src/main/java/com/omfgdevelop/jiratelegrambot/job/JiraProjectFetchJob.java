package com.omfgdevelop.jiratelegrambot.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omfgdevelop.jiratelegrambot.entity.ProjectEntity;
import com.omfgdevelop.jiratelegrambot.mapping.UberMapper;
import com.omfgdevelop.jiratelegrambot.repository.ProjectRepository;
import com.omfgdevelop.jiratelegrambot.view.jira.prject.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.FailedLoginException;
import java.util.List;

import static com.omfgdevelop.jiratelegrambot.Common.createHeaders;
import static com.omfgdevelop.jiratelegrambot.profiles.Profiles.TELEGRAM_FETCH_PROFILE;

@Service
@RequiredArgsConstructor
@Log4j2
@Profile(TELEGRAM_FETCH_PROFILE)
public class JiraProjectFetchJob {

    private final RestTemplate restTemplate;

    private final ProjectRepository projectRepository;

    private final UberMapper mapper;

    @Value("${jira.base.url}")
    private String baseUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void getJiraProfiles(String adminUsername, String adminPassword) throws JsonProcessingException, FailedLoginException {
        HttpHeaders headers = createHeaders(adminUsername, adminPassword);
        ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl + "/rest/api/2/project", HttpMethod.GET, new HttpEntity<Object>(headers), String.class);
        if (responseEntity.getStatusCode().value() == 200) {
            List<Project> projects = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<Project>>() {
            });
            projectRepository.saveAll(mapper.map(projects));

        } else if (responseEntity.getStatusCode().value() == 401) {
            throw new FailedLoginException(String.format("Failed to login with username %s", adminUsername));
        }
    }

}
