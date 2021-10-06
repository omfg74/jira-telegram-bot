package com.omfgdevelop.jiratelegrambot.service.jira;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omfgdevelop.jiratelegrambot.entity.Task;
import com.omfgdevelop.jiratelegrambot.entity.User;
import com.omfgdevelop.jiratelegrambot.exception.IssueCreateException;
import com.omfgdevelop.jiratelegrambot.service.TaskService;
import com.omfgdevelop.jiratelegrambot.service.UserService;
import com.omfgdevelop.jiratelegrambot.view.jira.issue.*;
import com.omfgdevelop.jiratelegrambot.view.jira.prject.Project;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.omfgdevelop.jiratelegrambot.Common.createHeaders;

@Service
@RequiredArgsConstructor
public class JiraIssueService {

    public final UserService userService;

    private final String apiUrl = "/rest/api/2";

    @Value("${jira.base.url}")
    private final String baseUrl;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TaskService taskService;


    public IssueResponse createIssue(Long telegramId, String taskTitle) throws JsonProcessingException, NotFoundException, IssueCreateException {
        User user = userService.getUserByUserId(telegramId);
        Task task = taskService.getCreatedTaskByTelegramIdAndTitle(telegramId, taskTitle);

        HttpHeaders headers = createHeaders(user.getJiraUsername(), user.getJiraPassword());

        Issuetype issuetype = new Issuetype();
        issuetype.setName("Bug");

        Project project = new Project();
        project.setKey("BOT");

        Fields fields = new Fields();
        fields.setSummary(task.getTaskTitle());
        fields.setDescription(task.getTaskText());
        fields.setProject(project);
        fields.setIssuetype(issuetype);

        Issue issue = new Issue();
        issue.setFields(fields);

        HttpEntity<Issue> request = new HttpEntity<>(issue, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + apiUrl + "/issue", request, String.class);

        if (response.getStatusCode().value() == 201) {
            taskService.markTaskAsDone(task);
            return objectMapper.readValue(response.getBody(), IssueResponse.class);
        }
        throw new IssueCreateException("Failed to create issue");

    }

}
