package br.com.taskapi.taskapi.task;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskUpdateRequest(
        @NotBlank @Size(max = 150) String title,
        String description,
        @NotNull Status status,
        @NotNull Priority priority,
        LocalDate dueDate
) {}