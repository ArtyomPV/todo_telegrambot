package ru.prusov.to_do_telegrambot.usecase.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.usecase.command.UserCommand;
import ru.prusov.to_do_telegrambot.usecase.inline.UserInlineCommand;
import ru.prusov.to_do_telegrambot.usecase.routers.InlineCommandRouter;
import ru.prusov.to_do_telegrambot.usecase.routers.CommandRouter;
import ru.prusov.to_do_telegrambot.usecase.routers.StateRouter;
import ru.prusov.to_do_telegrambot.usecase.state.UserState;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotService implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final String botToken;
    private final TelegramClient telegramClient;
    private final UserStateService userStateService;
    private final CommandRouter commandRouter;
    private final StateRouter stateRouter;
    private final InlineCommandRouter inlineCommandRouterRouter;
    private final PhotoService photoService;


    @Override
    public void consume(Update update) {

        if (update.hasInlineQuery()) {
            InlineQuery inlineQuery = update.getInlineQuery();
            CommonInfo commonInfo = getCommonInfo(inlineQuery);
            handleInLineQuery(commonInfo);
            return;
        }
        if (update.hasMessage()) {
            Message message = update.getMessage();
            CommonInfo commonInfo = getCommonInfo(message);
            if(message.hasText()) {
                String lastUserMessage = message.getText();

                if (lastUserMessage.startsWith("/")) {
                    handleCommand(lastUserMessage, commonInfo);
                    return;
                }

                long chatId = update.getMessage().getChatId();
                UserState userState = userStateService.getUserState(chatId);
                stateRouter.getHandler(userState).ifPresentOrElse(handler -> {
                    handler.handleState(commonInfo);
                }, () -> unknownActionHandler(chatId));
            } else if (message.hasPhoto()) {
                // Получаем список фото разных размеров
                List<PhotoSize> photos = message.getPhoto();
                // Берем самое большое фото
                PhotoSize photo = photos.get(photos.size() - 1);
                photoService.savePhotoFromMessage(photo, commonInfo.getChatId());


            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            System.out.println(data);
            CommonInfo commonInfo = getCommonInfo(callbackQuery);
            handleCommand(data, commonInfo);
        }

    }

    private void handleInLineQuery(CommonInfo commonInfo) {
        UserInlineCommand userInlineCommand = UserInlineCommand.fromString(commonInfo.getMessageText());
        inlineCommandRouterRouter.getHandler(userInlineCommand)
                .ifPresent(handler -> handler.execute(commonInfo));
    }

    private void handleCommand(String lastUserMessage, CommonInfo commonInfo) {
        UserCommand command = UserCommand.fromString(lastUserMessage);
        commandRouter.getHandler(command).ifPresentOrElse(handler -> {
                    userStateService.clearUserState(commonInfo.getChatId());
                    handler.execute(commonInfo);
                }
                , () -> unknownActionHandler(commonInfo.getChatId()));
    }

    @SneakyThrows
    private void unknownActionHandler(Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Я вас не понял. Начните с /start")
                .build();
        telegramClient.execute(sendMessage);
    }

    private CommonInfo getCommonInfo(Message message) {
        return CommonInfo.builder()
                .chatId(message.getChatId())
                .messageText(message.getText())
                .userFormTelegram(message.getFrom())
                .build();
    }

    private CommonInfo getCommonInfo(CallbackQuery callbackQuery) {
        return CommonInfo.builder()
                .userFormTelegram(callbackQuery.getFrom())
                .chatId(callbackQuery.getMessage().getChatId())
                .build();
    }

    private CommonInfo getCommonInfo(InlineQuery inlineQuery) {
        return CommonInfo.builder()
                .userFormTelegram(inlineQuery.getFrom())
                .chatId(inlineQuery.getFrom().getId())
                .messageText(inlineQuery.getQuery())
                .inlineQuery(inlineQuery)
                .build();


    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @AfterBotRegistration
    public void
    afterRegistration(BotSession botSession) {
        log.info("Registered bot running state is: {}", botSession.isRunning());
    }
}
