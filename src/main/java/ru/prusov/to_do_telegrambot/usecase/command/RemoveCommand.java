package ru.prusov.to_do_telegrambot.usecase.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.usecase.service.PlanService;
import ru.prusov.to_do_telegrambot.usecase.service.UserService;
import ru.prusov.to_do_telegrambot.usecase.service.UserStateService;
import ru.prusov.to_do_telegrambot.usecase.state.UserState;

@Component
@RequiredArgsConstructor
public class RemoveCommand implements Command {
    private final TelegramClient client;
    private final PlanService planService;
    private final UserStateService userStateService;
    private final UserService userService;

    @Override
    public UserCommand command() {
        return UserCommand.REMOVE;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        userStateService.setUserState(chatId, UserState.REMOVING_PLAN);
        String msg = "Введите номер задачи: ";
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(msg)
                .build();
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
