package ru.prusov.to_do_telegrambot.usecase.inline;

import org.springframework.scheduling.annotation.Async;
import ru.prusov.to_do_telegrambot.common.CommonInfo;

public interface InlineCommand {
    UserInlineCommand inlineCommand();
    @Async
    void execute(CommonInfo commonInfo);
}
