package com.omfgdevelop.jiratelegrambot.botapi;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.omfgdevelop.jiratelegrambot.config.AppConfig.REGISTERED_USER_SET;

@Service
@RequiredArgsConstructor
public class UserStateCache {

    private final Map<Long, UserState> userStates = new ConcurrentHashMap<>();

    @Qualifier(REGISTERED_USER_SET)
    private final Set<Long> registeredUsersState;


    public void setCurrentUserState(long userId, UserState userState) {
        userStates.put(userId, userState);
    }

    public UserState getCurrentUserState(Long userId) {
        UserState userState = userStates.get(userId);
        if (userState == null && !registeredUsersState.contains(userId)) {
            userState = UserState.UNREGISTERED;
        } else if (userState == null&&registeredUsersState.contains(userId)) {
            userState = UserState.STAND_BY;
        }
        return userState;
    }

    public boolean isRegistered(long userId) {
        return registeredUsersState.contains(userId);
    }
}
