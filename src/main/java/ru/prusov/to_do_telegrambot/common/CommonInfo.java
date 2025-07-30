package ru.prusov.to_do_telegrambot.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommonInfo {
    Long chatId;
    String messageText;
    User userFormTelegram;
    InlineQuery inlineQuery;
}
