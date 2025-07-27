package ru.prusov.to_do_telegrambot.usecase.routers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.usecase.state.State;
import ru.prusov.to_do_telegrambot.usecase.state.UserState;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StateRouter {
    Map<UserState, State> handlerMap = new EnumMap<>(UserState.class);

    public StateRouter(List<State> handlers) {
        handlers.forEach(h -> {
            handlerMap.put(h.state(), h);
        });
    }

    public Optional<State> getHandler(UserState userState) {
        return Optional.ofNullable(handlerMap.get(userState));
    }
}
