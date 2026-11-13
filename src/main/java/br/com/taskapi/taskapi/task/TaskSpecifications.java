package br.com.taskapi.taskapi.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

public final class TaskSpecifications {

    private TaskSpecifications() {}

    public static Specification<Task> withFilters(TaskFilter f) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (f.status() != null) {
                predicates.add(cb.equal(root.get("status"), f.status()));
            }
            if (f.priority() != null) {
                predicates.add(cb.equal(root.get("priority"), f.priority()));
            }
            if (f.dueFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dueDate"), f.dueFrom()));
            }
            if (f.dueTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dueDate"), f.dueTo()));
            }
            if (f.title() != null && !f.title().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")),
                        "%" + f.title().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}