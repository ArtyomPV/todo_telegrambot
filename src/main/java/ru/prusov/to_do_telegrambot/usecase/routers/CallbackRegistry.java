package ru.prusov.to_do_telegrambot.usecase.routers;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.prusov.to_do_telegrambot.utils.TelegramUtils.DELIMETER;

@Component
public class CallbackRegistry {

    private final Map<String, CallbackHandler> callbacks = new HashMap<>();

    public CallbackRegistry(List<CallbackHandler> callbackHandlerList){
        for(CallbackHandler handler : callbackHandlerList){
            String callbackHandlerName  = handler.getCallbackType().getCallbackName();
            callbacks.put(callbackHandlerName, handler);
        }
    }


    public CallbackHandler findCallbackForHandler(String callbackData) {
        CallbackHandler exactMatch = callbacks.get(callbackData);
        if(exactMatch != null && exactMatch.canHandle(callbackData)){
            return  exactMatch;
        }

        if(callbackData.contains(DELIMETER)){
            String prefix = callbackData.substring(0, callbackData.indexOf(DELIMETER));
            CallbackHandler prefixHandler = callbacks.get(prefix);
            if(prefixHandler != null && prefixHandler.canHandle(callbackData)){
                return prefixHandler;
            }
        }
        return null;
    }
}
