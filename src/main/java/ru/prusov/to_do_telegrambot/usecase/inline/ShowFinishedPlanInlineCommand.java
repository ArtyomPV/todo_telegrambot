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
import ru.prusov.to_do_telegrambot.usecase.state.PlanStatus;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ShowFinishedPlanInlineCommand implements InlineCommand {
    private final TelegramClient client;
    private final PlanService planService;

    @Override
    public UserInlineCommand inlineCommand() {
        return UserInlineCommand.SHOW_FINISHED_PLAN;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        List<Plan> plans = planService.getPlans(chatId);
        StringBuilder messageText = new StringBuilder("Список завершенных дел: \n");
        plans.stream()
                .filter(plan -> plan.getStatus().equals(PlanStatus.DONE))
                .forEach(plan -> {
                    messageText.append("ID-").append(plan.getId())
                            .append("-").append(plan.getTitle())
                            .append("\t").append(plan.getStatus())
                            .append("\n");
                });
        InputTextMessageContent content = new InputTextMessageContent(messageText.toString());
        InlineQueryResultArticle result = InlineQueryResultArticle.builder()
                .id(UUID.randomUUID().toString())
                .title("Показать все завершенные дела")
                .inputMessageContent(content)
                .description("Показ")
                .build();
        List<InlineQueryResultArticle> resultArticles = List.of(result);
        AnswerInlineQuery answer = AnswerInlineQuery.builder()
                .inlineQueryId(commonInfo.getInlineQuery().getId())
                .results(resultArticles)
                .cacheTime(3)
                .build();

        try {
            client.execute(answer);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
