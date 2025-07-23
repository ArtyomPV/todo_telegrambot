package ru.prusov.to_do_telegrambot.usecase.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.prusov.to_do_telegrambot.model.repository.UserRepository;
import ru.prusov.to_do_telegrambot.usecase.state.UserState;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserStateService {
    UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserState getUserState(long chatId){
        return userRepository.getUserStateByChatId(chatId);
    }

    @Transactional
    public void setUserState(long chatId, UserState userState){
        userRepository.setUserStateByChatId(userState, chatId);
    }

    @Transactional
    public void clearUserState(long chatId){
        userRepository.setUserStateByChatId(UserState.NONE, chatId);
    }

}
