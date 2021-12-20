package com.omfgdevelop.jiratelegrambot.entity;


import lombok.*;

import javax.persistence.*;

@Table(name = "group_chat")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupChat {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_telegram_id")
    private Long telegramId;

    @Column(name = "chat_telegram_name")
    private String chatName;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private ProjectEntity project;
}
