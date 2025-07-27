package ru.prusov.to_do_telegrambot.usecase.state;

import org.springframework.stereotype.Component;


public enum UserState {
    NONE,
    WAITING_PLAN,
    REMOVING_PLAN;
}
