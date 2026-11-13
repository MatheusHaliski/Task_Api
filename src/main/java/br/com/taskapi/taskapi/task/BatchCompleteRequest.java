package br.com.taskapi.taskapi.task;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BatchCompleteRequest(
        @NotEmpty List<@NotNull Long> ids
) {}