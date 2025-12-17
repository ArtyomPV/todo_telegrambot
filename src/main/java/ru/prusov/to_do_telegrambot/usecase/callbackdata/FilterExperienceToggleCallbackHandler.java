package ru.prusov.to_do_telegrambot.usecase.callbackdata;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.usecase.routers.UpdateCtx;

import static ru.prusov.to_do_telegrambot.usecase.callbackdata.BotCallbackEnum.FILTER_EXPERIENCE_TOGGLE;

@Component
@RequiredArgsConstructor
public class FilterExperienceToggleCallbackHandler extends AbstractCallbackHandler{

    private final TelegramClient telegramClient;

    @Override
    public BotCallbackEnum getCallbackType() {
        return FILTER_EXPERIENCE_TOGGLE;
    }

    @Override
    public boolean canHandle(String callbackData) {
        return callbackData != null && callbackData.startsWith(FILTER_EXPERIENCE_TOGGLE.getCallbackName());
    }

    @Override
    public void handle(UpdateCtx ctx) {


//        InlineKeyboardBuilder builder = InlineKeyboardBuilder.builder();
//
//        // Создаём кнопки с параметрами через delimiter
//        Arrays.stream(ExperienceEnum.values()).forEach(exp -> {
//            String buttonText = exp.getName();
//            // Формируем callback-данные: префикс + delimiter + параметр
//            String callbackData = "/filterExperienceToggle:" + exp.name();
//            builder.button(buttonText, callbackData).newRow();
//        });
//
//        InlineKeyboardMarkup keyboard = builder.build();
    }
}
