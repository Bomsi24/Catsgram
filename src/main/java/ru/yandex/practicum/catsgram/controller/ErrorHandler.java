package ru.yandex.practicum.catsgram.controller;

import lombok.Getter;

public class ErrorHandler {
        @Getter
        private final String error;
        private final String description;

        public ErrorHandler(String error, String description) {
            this.error = error;
            this.description = description;
        }
    }

