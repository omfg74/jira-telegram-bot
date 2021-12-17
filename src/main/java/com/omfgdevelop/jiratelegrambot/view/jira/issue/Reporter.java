package com.omfgdevelop.jiratelegrambot.view.jira.issue;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reporter {
    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("name")
    private String name;
}
