package ru.prusov.to_do_telegrambot.usecase.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.factory.KeyboardFactory;
import ru.prusov.to_do_telegrambot.model.entity.Plan;
import ru.prusov.to_do_telegrambot.usecase.service.PlanService;
import ru.prusov.to_do_telegrambot.usecase.service.UserService;

import java.util.List;

import static ru.prusov.to_do_telegrambot.usecase.callbackdata.CallbackData.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class ViewPlanState implements State {
    final TelegramClient client;
    final PlanService planService;
    final UserService userService;
    final KeyboardFactory keyboardFactory;

    @Override
    public UserState state() {
        return UserState.VIEW_PLAN;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        long planId = Long.parseLong(commonInfo.getMessageText());
        Plan plan = planService.getPlanByID(planId).orElseThrow();
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ")
                .append(plan.getId())
                .append("-")
                .append(plan.getDescription())
                .append("\n")
                .append("Status: ")
                .append(plan.getStatus());
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(sb.toString())
                .replyMarkup(keyboardFactory.getInlineKeyboard(
                        List.of("Назад"),
                        List.of(1),
                        List.of(LIST_BACK)
                ))
                .build();
        userService.changeUserState(chatId, UserState.NONE);
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
