package com.omfgdevelop.jiratelegrambot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "telegram_status")
@Getter
@Setter
public class TelegramStatus {

    @Id
    @Column(name = "id")
    private Long Id;

    @Column(name = "current_offset")
    private Long currentOffset;
}
