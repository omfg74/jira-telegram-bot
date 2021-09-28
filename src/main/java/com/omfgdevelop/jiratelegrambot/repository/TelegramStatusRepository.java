package com.omfgdevelop.jiratelegrambot.repository;

import com.omfgdevelop.jiratelegrambot.entity.TelegramStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramStatusRepository extends JpaRepository<TelegramStatus, Long> {
}
