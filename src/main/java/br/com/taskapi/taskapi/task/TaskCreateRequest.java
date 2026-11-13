package br.com.taskapi.taskapi.task;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskCreateRequest(
        @NotBlank @Size(max = 150) String title,
        String description,
        @NotNull Priority priority,
        @FutureOrPresent LocalDate dueDate
) {}