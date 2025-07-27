package ru.prusov.to_do_telegrambot.usecase.command;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.usecase.service.UserService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StartCommand implements Command {
    final TelegramClient telegramClient;
    final UserService userService;

    @Override
    public UserCommand command() {
        return UserCommand.START;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        userService.findOrCreateUser(chatId, commonInfo.getUserFormTelegram().getFirstName());
        SendMessage startMessage = SendMessage.builder()
                .chatId(chatId)
                .text("""
                        Привет! 👋
                        
                        Я бот-планировщик задач, который поможет вам организовать ваши дела!
                        
                        Что я умею:
                        📝 Добавлять новые задачи
                        🗑️ Удалять задачи
                        📋 Показывать весь список задач
                        ✅ Отмечать задачи как выполненные
                        
                        Начните использовать меня прямо сейчас!
                        """
                )
                .build();
        try {
            telegramClient.execute(startMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
