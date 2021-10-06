package com.omfgdevelop.jiratelegrambot.view.jira.auth;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Session {

    private String name;

    private String value;

}
