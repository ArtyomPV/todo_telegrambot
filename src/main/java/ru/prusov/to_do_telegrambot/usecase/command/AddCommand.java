package ru.prusov.to_do_telegrambot.usecase.command;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.usecase.service.UserStateService;
import ru.prusov.to_do_telegrambot.usecase.state.UserState;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddCommand implements Command{
    TelegramClient telegramClient;
    UserStateService userStateService;
    @Override
    public UserCommand command() {
        return UserCommand.ADD;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        long chatId = commonInfo.getChatId();
        SendMessage msg = SendMessage.builder()
                .chatId(chatId)
                .text("Запланируй дело")
                .build();
        try{
            telegramClient.execute(msg);
            userStateService.setUserState(chatId, UserState.WAITING_PLAN);
        } catch (TelegramApiException e){
            throw new RuntimeException();
        }
    }
}
