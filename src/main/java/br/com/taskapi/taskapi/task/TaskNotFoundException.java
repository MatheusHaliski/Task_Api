package br.com.taskapi.taskapi.task;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Task com id " + id + " não encontrada");
    }
}