package com.multiapp.boot.repository;

import com.multiapp.boot.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Spring Data generates the implementation at runtime. */
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByOrderByIdAsc();
}
