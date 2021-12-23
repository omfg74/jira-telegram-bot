package com.omfgdevelop.jiratelegrambot.repository;

import com.omfgdevelop.jiratelegrambot.entity.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupChat,Long> {

    @Query(value = "select * from group_chat where active = true and deleted = false", nativeQuery = true)
    Optional<List<GroupChat>> findAllRegistered();


    @Query(value = "select * from group_chat where active = true and deleted = false and chat_telegram_id=?1", nativeQuery = true)
    Optional<GroupChat> findRegisteredChatByTelegramId(Long chatId);

    @Query(value = "select * from group_chat where chat_telegram_id=?1", nativeQuery = true)
    Optional<GroupChat> findByChatId(Long id);

    @Query(value = "select * from group_chat where id=?1", nativeQuery = true)
    Optional<GroupChat> findRegisteredChatById(Long chatId);

    @Modifying
    @Query(value = "update group_chat set active=?2, project_id=?3 where id=?1",nativeQuery = true)
    void updateActiveAndProjectLink(Long chatId, Boolean active, String projectId);
}
