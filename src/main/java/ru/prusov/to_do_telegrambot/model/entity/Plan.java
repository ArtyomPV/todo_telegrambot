package ru.prusov.to_do_telegrambot.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "plans")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(length = 50)
    String title;
    String description;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    User user;

    public Plan(String description, User user){
        this.title = description.substring(0, Math.min(description.length(), 50));
        this.description = description;
        this.user = user;
    }
}
