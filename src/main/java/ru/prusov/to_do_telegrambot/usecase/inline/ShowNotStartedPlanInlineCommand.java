package ru.prusov.to_do_telegrambot.usecase.inline;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.model.entity.Plan;
import ru.prusov.to_do_telegrambot.usecase.service.PlanService;

import java.util.List;
import java.util.UUID;

import static ru.prusov.to_do_telegrambot.usecase.state.PlanStatus.NEED_TODO;

@Component
@RequiredArgsConstructor
public class ShowNotStartedPlanInlineCommand implements InlineCommand {
    private final PlanService planService;
    private final TelegramClient client;

    @Override
    public UserInlineCommand inlineCommand() {
        return UserInlineCommand.SHOW_NOT_STARTED_PLAN;
    }


    @Override
    public void execute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        List<Plan> plans = planService.getPlans(chatId);
        StringBuilder messageText = new StringBuilder("Список запланированных дел: \n\n");
            plans.stream()
                    .filter(plan1 -> plan1.getStatus().equals(NEED_TODO))
                    .forEach(plan -> {
                                messageText.append("ID-").append(plan.getId())
                                        .append("-").append(plan.getTitle())
                                        .append("\t").append(plan.getStatus())
                                        .append("\n");
                            }
                    );

        InputTextMessageContent content = new InputTextMessageContent(messageText.toString());
        InlineQueryResultArticle result = InlineQueryResultArticle.builder()
                .id(UUID.randomUUID().toString())
                .title("Показать все запланированные задачи")
                .inputMessageContent(content)
                .description("Показ")
                .build();
        List<InlineQueryResultArticle> results = List.of(result);
        AnswerInlineQuery answer = AnswerInlineQuery.builder()
                .inlineQueryId(commonInfo.getInlineQuery().getId())
                .results(results)
                .cacheTime(3)
                .build();

        try {
            client.execute(answer);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
