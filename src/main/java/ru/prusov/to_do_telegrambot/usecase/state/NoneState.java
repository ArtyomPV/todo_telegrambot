package ru.prusov.to_do_telegrambot.usecase.state;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoneState implements State{
    TelegramClient telegramClient;

    @Override
    public UserState state() {
        return UserState.NONE;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {
        log.info("NoneState: {}", commonInfo.getMessageText());
        SendMessage sendMessage = SendMessage.builder()
                .chatId(commonInfo.getChatId())
                .text("Не понимаю тебя!")
                .build();
        try{
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
