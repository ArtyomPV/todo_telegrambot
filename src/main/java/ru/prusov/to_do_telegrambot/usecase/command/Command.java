package ru.prusov.to_do_telegrambot.usecase.command;

import org.springframework.scheduling.annotation.Async;
import ru.prusov.to_do_telegrambot.common.CommonInfo;

public interface Command {
    UserCommand command();
    
    @Async
    void execute(CommonInfo commonInfo);
}
