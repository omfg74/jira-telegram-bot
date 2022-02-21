package com.omfgdevelop.jiratelegrambot.service.jira;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omfgdevelop.jiratelegrambot.entity.Task;
import com.omfgdevelop.jiratelegrambot.entity.User;
import com.omfgdevelop.jiratelegrambot.enums.TaskType;
import com.omfgdevelop.jiratelegrambot.exception.EcsEvent;
import com.omfgdevelop.jiratelegrambot.exception.IssueCreateException;
import com.omfgdevelop.jiratelegrambot.service.TaskService;
import com.omfgdevelop.jiratelegrambot.service.UserService;
import com.omfgdevelop.jiratelegrambot.view.jira.issue.*;
import com.omfgdevelop.jiratelegrambot.view.jira.project.Project;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidKeyException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;

import static com.omfgdevelop.jiratelegrambot.Common.createHeaders;

@Service
@RequiredArgsConstructor
@Log4j2
public class JiraIssueService {

    public final UserService userService;

    private final String apiUrl = "/rest/api/2";

    @Value("${jira.base.url}")
    private final String baseUrl;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TaskService taskService;

    @Value("${app.telegrambot.token}")
    private final String botToken;

    @Value("${app.telegrambot.url}")
    private final String botUrl;

    private final LinkedBlockingDeque<Task> queue;

    @Value("${app.reply.url}")
    private final String replyLink;

    @Value("${jira.admin.username}")
    private String adminUserName;

    @Value("${jira.admin.password}")
    private String adminPassword;


    private static final String CHAT_ID = "chat_id";

    public IssueResponse createIssue(Task task) throws JsonProcessingException, NotFoundException, IssueCreateException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        User user = userService.getUserByTelegramId(task.getTelegramId());

        HttpHeaders headers = createHeaders(adminUserName, adminPassword);

        Issuetype issuetype = new Issuetype();
        issuetype.setName("Task");

        Project project = new Project();
        project.setKey(task.getProject());

        Reporter reporter = new Reporter();
        reporter.setName(user.getJiraUsername());

        Fields fields = new Fields();
        fields.setSummary(task.getTaskTitle());
        fields.setDescription(task.getTaskText());
        fields.setProject(project);
        fields.setIssuetype(issuetype);
        fields.setReporter(reporter);

        Issue issue = new Issue();
        issue.setFields(fields);

        HttpEntity<Issue> request = new HttpEntity<>(issue, headers);
        log.debug(new EcsEvent("ISSUE URL").withContext("url", baseUrl + apiUrl + "/issue").withContext("headers", headers.toString()));
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + apiUrl + "/issue", request, String.class);

        if (response.getStatusCode().value() == 201) {
            taskService.markTaskAsDone(task);
            return objectMapper.readValue(response.getBody(), IssueResponse.class);
        } else if (response.getStatusCode().value() == 404) {
            log.error(new EcsEvent("Error 404 on issue create").withContext("response", response));
        }
        throw new IssueCreateException("Failed to create issue");

    }

    public void processCreation() {

        queue.addAll(taskService.getAllCreatedTasks());
        for (Task task : queue) {
            try {
                log.debug(new EcsEvent("Task updating").withContext("task", task.getTaskTitle()));
                taskService.markTaskAsProcessed(task);
                IssueResponse response = createIssue(task);
                Map<String, String> params = new HashMap<>();

                params.put("text", "Your+task+is+done+" + replyLink + response.getKey());
                if (Objects.equals(task.getTaskType(), TaskType.GROUP_CHAT_TASK.getValue())) {
                    params.put(CHAT_ID, task.getChatId().toString());
                } else {
                    params.put(CHAT_ID, task.getTelegramId().toString());
                }
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(botUrl + botToken + "/sendMessage");


                log.debug(new EcsEvent("Issue create url ").withContext("url", builder.build()));

                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.queryParam(entry.getKey(), entry.getValue());
                }
                HttpHeaders headers = new HttpHeaders();

                log.debug(new EcsEvent("ISSUE CREATED URL").withContext("url", builder.toUriString()).withContext("headers", headers.toString()));

                ResponseEntity<String> responseIssue = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(headers), String.class);

                if (responseIssue.getStatusCode().value() == 200) {
                    taskService.markTaskAsDone(task);
                    log.info(new EcsEvent("Task is done and sent to user").withContext("task_id", task.getId()).withContext("task_title", task.getTaskTitle()));
                }
                if (responseIssue.getStatusCode().value() == 404) {
                    log.error(new EcsEvent("Response 404").withContext("resp", response));
                }
                log.debug(new EcsEvent("Response").withContext("resp", response));
                queue.remove(task);
            } catch (Exception e) {
                e.printStackTrace();
                queue.remove(task);
                taskService.markTaskAsError(task);
                log.error(new EcsEvent("Error creating issue error").with(e).withContext("task", task.getTaskTitle()).withContext("task", task.getId()));
                sendErrorMessage(task, e);
            }
        }
    }

    private void sendErrorMessage(Task task, Exception e) {
        Map<String, String> params = new HashMap<>();
        taskService.markTaskAsError(task);
        params.put("text", "Error+creating+" + task.getTaskTitle() + "+because+" + e.getMessage());
        params.put("chat_id", task.getTelegramId().toString());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(botUrl + botToken + "/sendMessage");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        HttpHeaders headers = new HttpHeaders();
        restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity(headers), String.class);
    }
}
