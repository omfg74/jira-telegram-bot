package com.omfgdevelop.jiratelegrambot.view.jira.auth;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JiraAuthRequestView {

    private String username;

    private String password;
}
