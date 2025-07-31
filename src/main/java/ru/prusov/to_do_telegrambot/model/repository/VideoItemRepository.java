package ru.prusov.to_do_telegrambot.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.prusov.to_do_telegrambot.model.entity.VideoItem;

import java.util.List;

public interface VideoItemRepository extends JpaRepository<VideoItem, Long> {
    List<VideoItem> findByUserChatId(long chatId);
}
