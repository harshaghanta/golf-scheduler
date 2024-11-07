package com.golfacademy.resourceserver.lesson;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Lesson {
    private String title;
    private String description;
    private String instructor;
    private LocalDateTime schedule;
}
