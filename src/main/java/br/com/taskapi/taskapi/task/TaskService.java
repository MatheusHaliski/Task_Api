package br.com.taskapi.taskapi.task;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }
@Transactional(readOnly = true)
public Page<Task> search(TaskFilter filter, Pageable pageable) {
    return repository.findAll(TaskSpecifications.withFilters(filter), pageable);
}
    @Transactional
    public Task create(String title, String description, Priority priority, LocalDate dueDate) {
        Task task = new Task(title, description, priority, dueDate);
        return repository.save(task);
    }

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Task update(Long id, String title, String description,
                       Status status, Priority priority, LocalDate dueDate) {
        Task task = findById(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setPriority(priority);
        task.setDueDate(dueDate);
        return task;
    }
@Transactional
public List<Task> completeAll(List<Long> ids) {
    Set<Long> uniqueIds = new LinkedHashSet<>(ids);
    List<Task> tasks = repository.findAllById(uniqueIds);

    // 1) Todos os ids precisam existir
    if (tasks.size() != uniqueIds.size()) {
        Set<Long> found = tasks.stream().map(Task::getId).collect(Collectors.toSet());
        List<Long> missing = uniqueIds.stream().filter(id -> !found.contains(id)).toList();
        throw new TaskNotFoundException(missing);
    }

    // 2) Conclui uma a uma; se alguma já estiver DONE, lança e a transação faz rollback
    tasks.forEach(Task::complete);

    return tasks; // dirty checking grava tudo no commit
}
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        repository.deleteById(id);
    }
}