package ru.prusov.to_do_telegrambot.usecase.service;

import jakarta.transaction.RollbackException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.prusov.to_do_telegrambot.common.CommonInfo;
import ru.prusov.to_do_telegrambot.model.entity.Plan;
import ru.prusov.to_do_telegrambot.model.entity.User;
import ru.prusov.to_do_telegrambot.model.repository.PlanRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanService {
    final PlanRepository planRepository;
    final UserService userService;


    @Transactional
    public long savePlan(CommonInfo commonInfo) {
        if (commonInfo.getMessageText() == null || commonInfo.getMessageText().isEmpty()) {
            throw new RuntimeException("Message text is null or empty");
        }
        User user = userService.findOrCreateUser(commonInfo.getChatId(), commonInfo.getUserFormTelegram().getUserName());
        Plan plan = new Plan(commonInfo.getMessageText(), user);
        return planRepository.save(plan).getId();
    }

    @Transactional(readOnly = true)
    public List<Plan> getPlans(Long chatId) {
        return planRepository.findByUserChatId(chatId);
    }

    @Transactional(readOnly = true)
    public Optional<Plan> getPlanByID(long id) {
        return planRepository.findById(id);
    }

    @Transactional
    public void removePlan(long id) {
        Optional<Plan> plan = planRepository.findById(id);
        System.out.printf("Founded plan %s\n", plan.get());
        planRepository.delete(plan.get());
    }
}
