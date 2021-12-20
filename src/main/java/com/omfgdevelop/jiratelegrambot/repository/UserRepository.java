package com.omfgdevelop.jiratelegrambot.repository;

import com.omfgdevelop.jiratelegrambot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByTelegramId(Long telegramId);

    Optional<User> findByTelegramIdAndJiraUsername(Long telegramId, String telegramUsername);

    @Query(value = "select * from users where jira_username is not null and password_approved is true and active is true and deleted = false", nativeQuery = true)
    List<User> findAllWithUsernameNotNullAndPasswordNotNullAndActiveIsTrue();

    @Modifying
    @Query(value = "update users set deleted=true where telegram_id=?1", nativeQuery = true)
    void deleteByTelegramId(Long chatId);

}
