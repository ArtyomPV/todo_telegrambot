package ru.prusov.to_do_telegrambot.usecase.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Video;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.to_do_telegrambot.model.entity.VideoItem;
import ru.prusov.to_do_telegrambot.model.repository.VideoItemRepository;

@Service
@RequiredArgsConstructor
public class VideoItemService {
    private final TelegramClient client;
    private final UserService userService;
    private final VideoItemRepository videoItemRepository;

    public void saveVideoItemFromMessage(Video video, long chatId) {
        VideoItem videoItem = VideoItem.builder()
                .user(userService.getUserByChatId(chatId).get())
                .fieldId(video.getFileId())
                .fileName(video.getFileName())
                .build();
        videoItemRepository.save(videoItem);
    }
}
