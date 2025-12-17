package ru.prusov.to_do_telegrambot.usecase.routers;

import ru.prusov.to_do_telegrambot.usecase.callbackdata.BotCallbackEnum;

public interface  CallbackHandler {
    public BotCallbackEnum getCallbackType();

    public boolean canHandle(String callbackData);
    void handle(UpdateCtx ctx);
}
