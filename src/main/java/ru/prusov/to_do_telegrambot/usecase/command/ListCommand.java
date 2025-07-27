package ru.prusov.to_do_telegrambot.usecase.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.model.entity.Plan;
import ru.prusov.to_do_telegrambot.usecase.service.PlanService;
import ru.prusov.to_do_telegrambot.usecase.service.UserStateService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    final TelegramClient telegramClient;
    final PlanService planService;

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
                        .append(plan.getDescription())
                        .append("\n");
            }
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(messageBuilder.toString())
                    .build();

            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException();
        }
    }
}

