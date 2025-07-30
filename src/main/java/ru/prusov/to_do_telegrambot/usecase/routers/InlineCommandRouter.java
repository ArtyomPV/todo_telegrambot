package ru.prusov.to_do_telegrambot.usecase.routers;

import org.springframework.stereotype.Component;
import ru.prusov.to_do_telegrambot.usecase.inline.InlineCommand;
import ru.prusov.to_do_telegrambot.usecase.inline.UserInlineCommand;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InlineCommandRouter {
    private Map<UserInlineCommand, InlineCommand> handlerMap = new EnumMap<>(UserInlineCommand.class);

    public InlineCommandRouter(List<InlineCommand> handlers) {
        handlers.forEach(h -> handlerMap.put(h.inlineCommand(), h));
    }

    public Optional<InlineCommand> getHandler(UserInlineCommand userCommand) {
        return Optional.ofNullable(handlerMap.get(userCommand));

    }
}
