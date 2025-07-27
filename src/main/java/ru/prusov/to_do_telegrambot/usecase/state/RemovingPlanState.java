package ru.prusov.to_do_telegrambot.usecase.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.model.entity.User;
import ru.prusov.to_do_telegrambot.usecase.service.PlanService;
import ru.prusov.to_do_telegrambot.usecase.service.UserService;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemovingPlanState implements State {
    private final TelegramClient client;
    private final PlanService planService;
    private final UserService userService;

    @Override
    public UserState state() {
        return UserState.REMOVING_PLAN;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        log.info("RemovingPlanState: {}", commonInfo.getMessageText());
        Long id = Long.parseLong(commonInfo.getMessageText());
        planService.removePlan(id);
        String msgText = String.format(" Удалена задача с ID: %d", id);
        userService.changeUserState(chatId, UserState.NONE);
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
