package ru.prusov.to_do_telegrambot.usecase.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.factory.KeyboardFactory;
import ru.prusov.to_do_telegrambot.model.entity.Plan;
import ru.prusov.to_do_telegrambot.usecase.service.PlanService;
import ru.prusov.to_do_telegrambot.usecase.service.UserService;
import ru.prusov.to_do_telegrambot.usecase.state.UserState;

import java.util.List;

import static ru.prusov.to_do_telegrambot.usecase.callbackdata.CallbackData.*;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    final TelegramClient telegramClient;
    final PlanService planService;
    final UserService userService;
//    final KeyboardFactory keyboardFactory;

    @Override
    public UserCommand command() {
        return UserCommand.LIST;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        long chatId = commonInfo.getChatId();
        try {
            List<Plan> plans = planService.getPlans(chatId);

            if (plans.isEmpty() || plans == null) {
                SendMessage nothingMessage = new SendMessage(
                        commonInfo.getChatId().toString(),
                        "У вас пока нет запланированных дел ✅"
                );
                telegramClient.execute(nothingMessage);
                return;
            }
            StringBuilder messageBuilder = new StringBuilder();
            for (Plan plan : plans) {
                messageBuilder.append("ID: ")
                        .append(plan.getId())
                        .append("-")
                        .append(plan.getTitle())
                        .append(" status: ")
                        .append(plan.getStatus())
                        .append("\n");
            }
            messageBuilder.append("\n")
                    .append("Для просмотра задачи выберите ID");
            userService.changeUserState(chatId, UserState.VIEW_PLAN);

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(messageBuilder.toString())
                    .replyMarkup(KeyboardFactory.getInlineKeyboard(
                            List.of("Удалить задачу", "Отметить задачу", "Назад"),
                            List.of(2, 1),
                            List.of(REMOVE_PLAN, CHANGE_PLAN_STATUS, START_BACK)
                    ))
                    .build();

            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException();
        }
    }
}

