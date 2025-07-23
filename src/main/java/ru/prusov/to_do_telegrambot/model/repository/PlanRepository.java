package ru.prusov.to_do_telegrambot.model.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.prusov.to_do_telegrambot.model.entity.Plan;

import java.util.List;

@Transactional
public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByUserChatId(long chatId);
}
