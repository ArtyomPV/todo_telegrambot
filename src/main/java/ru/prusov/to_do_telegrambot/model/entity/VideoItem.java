package ru.prusov.to_do_telegrambot.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "videos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String fileName;
    String fieldId;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    User user;
}
