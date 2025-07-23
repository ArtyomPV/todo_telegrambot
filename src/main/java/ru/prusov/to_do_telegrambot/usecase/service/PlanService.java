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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanService {
    final PlanRepository planRepository;
    final UserService userService;

    @Transactional
    public long savePlan(CommonInfo commonInfo){
        if(commonInfo.getMessageText()==null || commonInfo.getMessageText().isEmpty()){
            throw new RuntimeException("Message text is null or empty");
        }
        User user = userService.findOrCreateUser(commonInfo.getChatId(), commonInfo.getUserFormTelegram().getUserName());
        Plan plan = new Plan(commonInfo.getMessageText(), user);
        return planRepository.save(plan).getId();
    }

    @Transactional(readOnly = true)
    public List<Plan> getPlans(Long chatId){
        return planRepository.findByUserChatId(chatId);
    }
}
