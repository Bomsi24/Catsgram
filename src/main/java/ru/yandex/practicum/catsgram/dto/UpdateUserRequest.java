package ru.yandex.practicum.catsgram.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String username;
    private String email;
    private String password;

    public boolean hasEmail() {
        return email != null && !email.isEmpty();
    }

    public boolean hasPassword() {
        return email != null && !email.isEmpty();
    }

    public boolean hasUsername() {
        return email != null && !email.isEmpty();
    }

}
