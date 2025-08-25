package com.desafio.taskservice.repository;

import com.desafio.taskservice.model.Task;
import com.desafio.taskservice.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    long countByUserId(Long userId);

    List<Task> findByStatus(TaskStatus status);
    List<Task> findByUserId(Long userId);
    List<Task> findByStatusAndUserId(TaskStatus status, Long userId);
}
