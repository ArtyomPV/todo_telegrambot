package ru.prusov.to_do_telegrambot.usecase.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.usecase.service.UserService;
import ru.prusov.to_do_telegrambot.usecase.service.UserStateService;
import ru.prusov.to_do_telegrambot.usecase.state.UserState;

@Component
@RequiredArgsConstructor
public class DoneCommand implements Command {
    private final TelegramClient client;
    private final UserStateService userStateService;

    @Override
    public UserCommand command() {
        return UserCommand.DONE;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        userStateService.setUserState(chatId, UserState.DONE_PLAN);
        String msgText = "Введите ID задачи, которую выполнили: ";
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(msgText)
                .build();
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
