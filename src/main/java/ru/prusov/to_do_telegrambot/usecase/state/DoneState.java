package ru.prusov.to_do_telegrambot.usecase.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.model.entity.Plan;
import ru.prusov.to_do_telegrambot.usecase.service.PlanService;
import ru.prusov.to_do_telegrambot.usecase.service.UserService;

@Component
@Slf4j
@RequiredArgsConstructor
public class DoneState implements State {
    private final TelegramClient client;
    private final UserService userService;
    private final PlanService planService;

    @Override
    public UserState state() {
        return UserState.DONE_PLAN;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        Plan planByID = planService.getPlanByID(Long.parseLong(commonInfo.getMessageText())).get();
        planByID.setStatus(PlanStatus.DONE);
        planService.save(planByID);
        userService.changeUserState(chatId, UserState.NONE);
        String msgText = String.format("%s установлен статус: __задача выполнена__", planByID.getDescription());
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(msgText)
                .parseMode("markdownV2")
                .build();
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
