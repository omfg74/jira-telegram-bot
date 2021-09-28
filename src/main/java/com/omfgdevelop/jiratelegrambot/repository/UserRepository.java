package com.omfgdevelop.jiratelegrambot.repository;

import com.omfgdevelop.jiratelegrambot.entity.User;
import org.checkerframework.common.util.report.qual.ReportUse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByTelegramId(Long telegramId);

    Optional<User> findByTelegramIdAndJiraUsername(Long telegramId, String telegramUsername);

    @Query(value = "select * from users where jira_username is not null and jira_password is not null ", nativeQuery = true)
    List<User> findAllWithUsernameNotNullAndPasswordNotNull();

}
