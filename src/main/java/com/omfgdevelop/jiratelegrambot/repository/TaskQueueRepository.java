package com.omfgdevelop.jiratelegrambot.repository;

import com.omfgdevelop.jiratelegrambot.entity.Task;
import com.omfgdevelop.jiratelegrambot.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskQueueRepository extends JpaRepository<Task, Long> {


    @Query(value = "select * from tasks_queue t  where t.telegram_id =:telegram_id and (t.status=4 or t.status=5) limit 1", nativeQuery = true)
    Optional<Task> findByTelegramIdAndStatusNewTitleOrNewText(@Param("telegram_id") Long telegramId);

    Optional<Task> findByTelegramIdAndStatus(Long telegramId, Integer status);

}
