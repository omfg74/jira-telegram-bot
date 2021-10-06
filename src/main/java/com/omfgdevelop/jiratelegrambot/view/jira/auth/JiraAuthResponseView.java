package com.omfgdevelop.jiratelegrambot.view.jira.auth;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JiraAuthResponseView {

    private Session session;

    private LoginInfo loginInfo;

}
