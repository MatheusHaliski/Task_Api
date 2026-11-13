package br.com.taskapi.taskapi.task;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Task com id " + id + " não encontrada");
    }
    public TaskNotFoundException(java.util.Collection<Long> ids) {
    super("Tasks não encontradas: " + ids);
}
}