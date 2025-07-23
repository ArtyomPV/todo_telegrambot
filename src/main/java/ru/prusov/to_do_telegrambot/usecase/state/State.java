package ru.prusov.to_do_telegrambot.usecase.state;

import org.springframework.scheduling.annotation.Async;
import ru.prusov.to_do_telegrambot.common.CommonInfo;

public interface State {
    UserState state();
    @Async
    void handleState(CommonInfo commonInfo);
}
