package ru.prusov.to_do_telegrambot.usecase.inline;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserInlineCommand {
    SHOW_NOT_STARTED_PLAN("Новые дела"),
    SHOW_FINISHED_PLAN("Завершенные дела");
    private final String command;

    public static UserInlineCommand fromString(String command){
        for(UserInlineCommand userInlineCommand: UserInlineCommand.values()){
            if(userInlineCommand.command.equals(command)){
                return userInlineCommand;
            }
        }
        return null;
    }
}
