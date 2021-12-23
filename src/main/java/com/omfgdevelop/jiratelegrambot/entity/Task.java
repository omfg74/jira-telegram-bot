package com.omfgdevelop.jiratelegrambot.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tasks_queue")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "task_type")
    private Integer taskType;

    @Column(name = "telegram_id")
    private Long telegramId;

    @Column(name = "params")
    private String params;

    @Column(name = "status")
    private Integer status;

    @Column(name = "task_title")
    private String taskTitle;

    @Column(name = "task_text")
    private String taskText;

    @Column(name = "task_responsible")
    private String taskResponsible;

    @Column(name = "project")
    private String project;

    @Column(name = "chat_id")
    private Long chatId;


}
