package ru.prusov.to_do_telegrambot.usecase.routers;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.prusov.to_do_telegrambot.usecase.state.BotState;
@Getter
public class UpdateCtx {

    private final String callbackData;
    private final Update update;

    public UpdateCtx (Update update, BotState currentState){
        this.callbackData = update.getCallbackQuery().getData();
        this.update = update;
    }


}
