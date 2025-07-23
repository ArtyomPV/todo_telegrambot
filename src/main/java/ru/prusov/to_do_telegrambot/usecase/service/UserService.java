package ru.prusov.to_do_telegrambot.usecase.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prusov.to_do_telegrambot.model.entity.User;
import ru.prusov.to_do_telegrambot.model.repository.UserRepository;
import ru.prusov.to_do_telegrambot.usecase.state.UserState;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User findOrCreateUser(long chatId, String username){
        return userRepository.getUserByChatId(chatId).orElseGet(()->{
            User user = User.builder()
                    .id(chatId)
                    .name(username)
                    .userState(UserState.NONE)
                    .build();
            userRepository.save(user);
            return user;
        });
    }
}
