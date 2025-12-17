package ru.prusov.to_do_telegrambot.usecase.routers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.prusov.to_do_telegrambot.usecase.state.BotState;

@Component
@AllArgsConstructor
public class CallbackRouter {

    private final CallbackRegistry callbackRegistry;

    public void route(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        CallbackHandler handler = callbackRegistry.findCallbackForHandler(callbackData);

        if(handler != null){
            UpdateCtx ctx = new UpdateCtx(update, BotState.IDLE);
            handler.handle(ctx);
        }
    }
}
