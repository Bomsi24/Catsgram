package ru.yandex.practicum.catsgram.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class NewUserRequest {
    private String username;
    private String email;
    private String password;
    private Instant registrationDate;
}
