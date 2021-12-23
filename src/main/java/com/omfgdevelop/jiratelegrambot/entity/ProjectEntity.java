package com.omfgdevelop.jiratelegrambot.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "jira_project")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "expand")
    private String expand;

    @Column(name = "self")
    private String self;

    @Column(name = "key")
    private String key;

    @Column(name = "name")
    private String name;

    @Column(name = "project_key")
    private String projectTypeKey;

    @Column(name = "display")
    private Boolean display;
}
