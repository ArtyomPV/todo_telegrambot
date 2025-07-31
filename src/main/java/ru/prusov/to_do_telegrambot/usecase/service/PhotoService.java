package ru.prusov.to_do_telegrambot.usecase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.model.entity.Photo;
import ru.prusov.to_do_telegrambot.model.repository.PhotoRepository;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService {
    private final String botToken;
    private final TelegramClient client;
    private final PhotoRepository photoRepository;
    private final UserService userService;

    public void savePhotoFromMessage(PhotoSize photoSize, long chatId) {
        try {
            GetFile getFileMethod = new GetFile(photoSize.getFileId());
            File file = client.execute(getFileMethod);
            log.info(file.getFileId());
            Photo photo = new Photo();
            photo.setPhotoField(file.getFileId());
            photo.setUser(userService.getUserByChatId(chatId).get());
            photoRepository.save(photo);
            String fileUrl = "https://api.telegram.org/file/bot" + botToken + "/" + file.getFilePath();
            log.info(fileUrl);

            SendMessage messageText = SendMessage.builder()
                    .chatId(chatId)
                    .text("Данное изображение сохранено.")
                    .build();
            client.execute(messageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<Photo> getAllPhotos(long chatId) {
        return photoRepository.findByUserChatId(chatId);
    }
}
