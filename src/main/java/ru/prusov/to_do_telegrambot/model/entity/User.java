package ru.prusov.to_do_telegrambot.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.prusov.to_do_telegrambot.usecase.state.UserState;

@Data
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    long chatId;
    String name;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    UserState state = UserState.NONE;
}