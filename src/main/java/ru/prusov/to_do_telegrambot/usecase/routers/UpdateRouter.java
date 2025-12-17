package ru.prusov.to_do_telegrambot.usecase.routers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@AllArgsConstructor
public class UpdateRouter {
    private final CallbackRouter callbackRouter;

    public void route(Update update){
        if(update.hasCallbackQuery()){
            callbackRouter.route(update);
            return;
        }
    }
}
