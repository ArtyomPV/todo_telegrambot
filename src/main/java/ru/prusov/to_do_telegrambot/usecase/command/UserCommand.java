package ru.prusov.to_do_telegrambot.usecase.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserCommand {
    ADD("/add"),
    START("/start"),
    LIST("/list"),
    REMOVE("/remove");

    private final String command;

    public static UserCommand fromString(String command) {
        for (UserCommand userCommand : UserCommand.values()) {
            if (userCommand.command.equals(command)) {
                return userCommand;
            }
        }
        return null;
    }
}
