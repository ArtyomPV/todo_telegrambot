package ru.prusov.to_do_telegrambot.usecase.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.prusov.to_do_telegrambot.model.repository.UserRepository;
import ru.prusov.to_do_telegrambot.usecase.state.UserState;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserStateService {
    final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserState getUserState(long chatId) {
        return userRepository.getUserStateByChatId(chatId);
    }

    @Transactional
    public void setUserState(long chatId, UserState userState) {
        userRepository.setUserStateByChatId(chatId, userState);
    }

    @Transactional
    public void clearUserState(long chatId) {
        userRepository.setUserStateByChatId(chatId, UserState.NONE);
    }

}
