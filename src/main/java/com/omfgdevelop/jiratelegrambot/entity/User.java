package com.omfgdevelop.jiratelegrambot.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "telegram_id")
    private Long telegramId;

    @Column(name = "telegram_username")
    private String telegramUsername;

    @Column(name = "jira_username")
    private String jiraUsername;

    @Column(name = "jira_password")
    private String jiraPassword;

    @Column(name = "jira_session_token")
    private String jiraSessionToken;

    @Column(name = "self")
    private String self;

    @Column(name = "key")
    private String key;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String emailAddress;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "active")
    private Boolean active;
}
