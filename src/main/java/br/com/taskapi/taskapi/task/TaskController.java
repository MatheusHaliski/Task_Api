package br.com.taskapi.taskapi.task;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.taskapi.taskapi.common.PageResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskCreateRequest request) {
        Task task = service.create(
                request.title(),
                request.description(),
                request.priority(),
                request.dueDate()
        );
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(task.getId())
                .toUri();
        return ResponseEntity.created(location).body(TaskResponse.from(task));
    }

    @GetMapping("/{id}")
    public TaskResponse findById(@PathVariable Long id) {
        return TaskResponse.from(service.findById(id));
    }
@PatchMapping("/complete")
public List<TaskResponse> completeAll(@Valid @RequestBody BatchCompleteRequest request) {
    return service.completeAll(request.ids()).stream()
            .map(TaskResponse::from)
            .toList();
}
   @GetMapping
public PageResponse<TaskResponse> search(
        @RequestParam(required = false) Status status,
        @RequestParam(required = false) Priority priority,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueFrom,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueTo,
        @RequestParam(required = false) String title,
        @ParameterObject
        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

    TaskFilter filter = new TaskFilter(status, priority, dueFrom, dueTo, title);
    return PageResponse.from(service.search(filter, pageable).map(TaskResponse::from));
}

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id,
                               @Valid @RequestBody TaskUpdateRequest request) {
        Task task = service.update(
                id,
                request.title(),
                request.description(),
                request.status(),
                request.priority(),
                request.dueDate()
        );
        return TaskResponse.from(task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}