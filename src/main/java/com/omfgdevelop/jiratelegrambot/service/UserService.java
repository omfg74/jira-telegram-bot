package com.omfgdevelop.jiratelegrambot.service;

import com.omfgdevelop.jiratelegrambot.entity.User;
import com.omfgdevelop.jiratelegrambot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.transaction.Transactional;
import java.security.InvalidKeyException;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Set<Long> registeredIds;

    private final UserRepository userRepository;

    private final EncryptionService encryptionService;

    public Boolean checkIfRegistered(long userId) {
        if (registeredIds.contains(userId)) {
            return true;
        }
        Optional<User> user = userRepository.findByTelegramId(userId);
        return user.isPresent();
    }

    public User getUserByTelegramId(Long id) {
        Optional<User> user = userRepository.findByTelegramId((long) id);
        return user.orElse(null);
    }

    public boolean createOrUpdate(User user) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        Optional<User> entity = userRepository.findByTelegramId(user.getTelegramId());
        if (entity.isPresent() && Boolean.TRUE.equals(!entity.get().getActive())) return true;
        User userToCreate = new User();
        if (entity.isPresent()) {
            userToCreate = entity.get();
        }
        userToCreate.setJiraUsername(user.getJiraUsername() != null ? user.getJiraUsername() : userToCreate.getJiraUsername());
        userToCreate.setTelegramId(user.getTelegramId() != null ? user.getTelegramId() : userToCreate.getTelegramId());
        userToCreate.setJiraPassword(user.getJiraPassword() != null ? encryptionService.encrypt(user.getJiraPassword()) : userToCreate.getJiraPassword());
        userToCreate.setTelegramUsername(user.getTelegramUsername() != null ? user.getTelegramUsername() : userToCreate.getTelegramUsername());
        userRepository.save(userToCreate);
        return false;
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }


    @Transactional
    public void deleteUser(Long chatId) {
        userRepository.deleteByTelegramId(chatId);
    }
}
