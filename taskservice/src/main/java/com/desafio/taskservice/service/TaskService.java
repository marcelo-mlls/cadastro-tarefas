package com.desafio.taskservice.service;

import com.desafio.taskservice.model.Task;
import com.desafio.taskservice.model.TaskStatus;
import com.desafio.taskservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> findTasks(TaskStatus status, Long userId){
        if(status != null && userId != null){
            return taskRepository.findByStatusAndUserId(status,userId);
        } else if (status != null) {
            return taskRepository.findByStatus(status);
        }else {
            return taskRepository.findByUserId(userId);
        }
    }

    public Optional<Task> getTaskById(Long id){
        return taskRepository.findById(id);
    }

    public Task createTask(Task task){
        task.setStatus(TaskStatus.PENDENTE);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails){
        Task task = taskRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Tarefa não encontrada com o id: " + id));

        if(task.getStatus() == TaskStatus.CONCLUIDO){
            throw new IllegalStateException("Não é possível editar uma tarefa concluída. ");
        }

        task.setTitulo(taskDetails.getDescricao());
        task.setDescricao(taskDetails.getDescricao());
        task.setStatus(taskDetails.getStatus());
        task.setDataLimite(taskDetails.getDataLimite());

        return taskRepository.save(task);
    }

    public void deleteTask(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Tarefa não encontrada com id: " + id) );

        taskRepository.delete(task);
    }

    public long countTasksByUserId(Long userId){
        return taskRepository.countByUserId(userId);
    }
}
