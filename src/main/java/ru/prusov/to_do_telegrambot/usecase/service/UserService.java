package ru.prusov.to_do_telegrambot.usecase.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.model.entity.User;
import ru.prusov.to_do_telegrambot.model.repository.UserRepository;
import ru.prusov.to_do_telegrambot.usecase.state.State;
import ru.prusov.to_do_telegrambot.usecase.state.UserState;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User findOrCreateUser(long chatId, String username) {
        return userRepository.getUserByChatId(chatId).orElseGet(() -> {
            User user = new User();
            user.setName(username);
            user.setChatId(chatId);
            return userRepository.save(user);
        });
    }

    @Transactional()
    public Optional<User> getUserByChatId(Long chatId) {
        return userRepository.getUserByChatId(chatId);
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void changeUserState(Long chatId, UserState userState) {
        User user = getUserByChatId(chatId).get();
        user.setState(userState);
        saveUser(user);
    }
}
