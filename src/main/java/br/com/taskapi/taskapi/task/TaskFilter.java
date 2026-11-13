package br.com.taskapi.taskapi.task;

import java.time.LocalDate;

public record TaskFilter(
        Status status,
        Priority priority,
        LocalDate dueFrom,
        LocalDate dueTo,
        String title
) {}