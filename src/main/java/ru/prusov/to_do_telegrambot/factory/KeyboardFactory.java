package ru.prusov.to_do_telegrambot.factory;

import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;


@UtilityClass
public class KeyboardFactory {
    public static InlineKeyboardMarkup getInlineKeyboard(List<String> text,
                                                  List<Integer> configuration,
                                                  List<String> data) {
        List<InlineKeyboardRow> keyboard = new ArrayList<>();
        int index = 0;
        for (Integer rowNumber : configuration) {
            InlineKeyboardRow row = new InlineKeyboardRow();

            for (int i = 0; i < rowNumber; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton(text.get(index));
                button.setCallbackData(data.get(index));
                row.add(button);
                index++;
            }
            keyboard.add(row);
        }
        return new InlineKeyboardMarkup(keyboard);
    }
}
