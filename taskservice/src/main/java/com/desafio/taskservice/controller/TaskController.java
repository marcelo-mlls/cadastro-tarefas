package com.desafio.taskservice.controller;

import com.desafio.taskservice.model.Task;
import com.desafio.taskservice.model.TaskStatus;
import com.desafio.taskservice.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> findTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Long userId){
        return taskService.findTasks(status,userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task){
        if(task.getUserId() == null){
            return ResponseEntity.badRequest().build();
        }
        Task createTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody Task taskDetails){
        try {
            taskService.updateTask(id, taskDetails);
        }catch (IllegalArgumentException e){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (IllegalStateException e){
            ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return ResponseEntity.ok("Tarefa atualizada com sucesso. ");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        try {
            taskService.deleteTask(id);
        }catch (IllegalArgumentException e){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count-by-user/{id}")
    public ResponseEntity<Long> countTaskByUser(@PathVariable Long userId){
        return ResponseEntity.ok(taskService.countTasksByUserId(userId));
    }
}
