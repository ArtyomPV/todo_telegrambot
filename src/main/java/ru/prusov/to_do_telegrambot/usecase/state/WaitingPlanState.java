package ru.prusov.to_do_telegrambot.usecase.state;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.usecase.service.PlanService;
import ru.prusov.to_do_telegrambot.usecase.service.UserStateService;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WaitingPlanState implements State{
    TelegramClient telegramClient;
    PlanService planService;
    UserStateService userStateService;

    @Override
    public UserState state() {
        return UserState.WAITING_PLAN;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {
        log.info("WaitingPlanState: {}", commonInfo.getMessageText());
        long id = planService.savePlan(commonInfo);
        String text = "Твой план записан. ID: " + id;
        SendMessage sendMessage = SendMessage.builder()
                .chatId(commonInfo.getChatId())
                .text(text)
                .build();
        userStateService.setUserState(commonInfo.getChatId(), UserState.NONE);
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
