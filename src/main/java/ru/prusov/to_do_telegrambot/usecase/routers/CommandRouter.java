package ru.prusov.to_do_telegrambot.usecase.routers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.prusov.to_do_telegrambot.usecase.command.Command;
import ru.prusov.to_do_telegrambot.usecase.command.UserCommand;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommandRouter {
    Map<UserCommand, Command> handlerMap = new EnumMap<>(UserCommand.class);

    public CommandRouter(List<Command> handlers){
        handlers.forEach(h->{
            handlerMap.put(h.command(),h);
        });
    }

    public Optional<Command> getHandler(UserCommand userCommand) {
        return Optional.ofNullable(handlerMap.get(userCommand));
    }

}
