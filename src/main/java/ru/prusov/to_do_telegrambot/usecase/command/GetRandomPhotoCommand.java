package ru.prusov.to_do_telegrambot.usecase.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.model.entity.Photo;
import ru.prusov.to_do_telegrambot.usecase.service.PhotoService;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class GetRandomPhotoCommand implements Command {
    private final TelegramClient client;
    private final PhotoService photoService;

    @Override
    public UserCommand command() {
        return UserCommand.PHOTO;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        try {
            List<Photo> allPhotos = photoService.getAllPhotos(chatId);
            if (allPhotos.isEmpty()) {
                SendMessage nothingMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text("У вас нет сохраненных фото \uD83E\uDEE3")
                        .build();
                client.execute(nothingMessage);
                return;
            }
            Photo randomPhoto = getRandomPhoto(allPhotos);
            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(chatId)
                    .caption("Случайно выбранное фото")
                    .photo(new InputFile(randomPhoto.getPhotoField()))
                    .build();
            client.execute(sendPhoto);
        } catch (
                TelegramApiException e) {
            throw new RuntimeException();
        }
    }

    private Photo getRandomPhoto(List<Photo> allPhotos) {
        return allPhotos.get(new Random().nextInt(allPhotos.size()));
    }
}
