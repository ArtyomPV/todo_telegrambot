package ru.prusov.to_do_telegrambot.usecase.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.model.entity.Plan;
import ru.prusov.to_do_telegrambot.model.entity.User;
import ru.prusov.to_do_telegrambot.usecase.service.PlanService;
import ru.prusov.to_do_telegrambot.usecase.service.UserService;
import ru.prusov.to_do_telegrambot.usecase.state.UserState;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemoveCommand implements Command {
    private final TelegramClient client;
    private final PlanService planService;
    private final UserService userService;

    @Override
    public UserCommand command() {
        return UserCommand.REMOVE;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        User user = userService.getUserByChatId(chatId).get();
        user.setState(UserState.REMOVING_PLAN);
        System.out.println(user);
        userService.saveUser(user);
        String msg = "Введите номер задачи: ";
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(msg)
                .parseMode("MarkdownV2")
                .build();
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
