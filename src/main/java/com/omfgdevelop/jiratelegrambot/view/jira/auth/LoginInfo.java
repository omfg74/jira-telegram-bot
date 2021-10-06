package com.omfgdevelop.jiratelegrambot.view.jira.auth;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfo {

    private Integer failedLoginCount;

    private Integer loginCount;

    private Timestamp lastFailedLoginTime;

    private Timestamp previousLoginTime;
}
