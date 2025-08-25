package com.desafio.taskservice.controller;

import com.desafio.taskservice.model.Task;
import com.desafio.taskservice.model.TaskStatus;
import com.desafio.taskservice.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task1;

    @BeforeEach
    public void setUp() {
        task1 = new Task();
        task1.setId(1L);
        task1.setTitulo("Test Task");
        task1.setDescricao("Test Description");
        task1.setStatus(TaskStatus.PENDENTE);
        task1.setUserId(1L);
        task1.setDataCriacao(LocalDateTime.now());
    }

    @Test
    public void whenGetTasks_thenReturnJsonArray() throws Exception {
        given(taskService.findTasks(null, null)).willReturn(Collections.singletonList(task1));

        mvc.perform(get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo", is(task1.getTitulo())));
    }

    @Test
    public void whenGetTasksWithFilter_thenReturnFilteredJsonArray() throws Exception {
        given(taskService.findTasks(eq(TaskStatus.PENDENTE), eq(1L))).willReturn(Collections.singletonList(task1));

        mvc.perform(get("/api/tasks")
                        .param("status", "PENDENTE")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo", is(task1.getTitulo())));
    }

    @Test
    public void whenPostTask_thenCreateTask() throws Exception {
        given(taskService.createTask(any(Task.class))).willReturn(task1);

        mvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo", is(task1.getTitulo())));
    }

    @Test
    public void whenPutTask_thenUpdateTask() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setTitulo("Updated Task");
        updatedTask.setStatus(TaskStatus.CONCLUIDO);

        given(taskService.updateTask(eq(1L), any(Task.class))).willReturn(updatedTask);

        mvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(content().string("Tarefa atualizada com sucesso."));
    }

    @Test
    public void whenDeleteTask_thenDeleteTask() throws Exception {
        mvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
