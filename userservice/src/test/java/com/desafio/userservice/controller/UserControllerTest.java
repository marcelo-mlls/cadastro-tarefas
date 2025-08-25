package com.desafio.userservice.controller;

import com.desafio.userservice.controller.UserController;
import com.desafio.userservice.model.User;
import com.desafio.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setNome("Marcelo");
        user1.setEmail("marcelo@test.com");
        user1.setDataCriacao(LocalDateTime.now());

        user2 = new User();
        user2.setId(2L);
        user2.setNome("Jules");
        user2.setEmail("jules@test.com");
        user2.setDataCriacao(LocalDateTime.now());
    }

    @Test
    public void whenGetAllUsers_thenReturnJsonArray() throws Exception {
        given(userService.getAllUsers()).willReturn(Arrays.asList(user1, user2));

        mvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome", is(user1.getNome())));
    }

    @Test
    public void whenGetUserById_thenReturnUser() throws Exception {
        given(userService.getUserById(1L)).willReturn(Optional.of(user1));

        mvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is(user1.getNome())));
    }

    @Test
    public void whenPostUser_thenCreateUser() throws Exception {
        User newUser = new User();
        newUser.setNome("New User");
        newUser.setEmail("new@test.com");

        given(userService.createUser(any(User.class))).willReturn(newUser);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuário criado com sucesso. ID: " + newUser.getId()));
    }

    @Test
    public void whenPutUser_thenUpdateUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setNome("Updated Marcelo");
        updatedUser.setEmail("marcelo_updated@test.com");

        given(userService.updateUser(eq(1L), any(User.class))).willReturn(updatedUser);

        mvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário atualizado com sucesso."));
    }

    @Test
    public void whenDeleteUser_thenDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário deletado com sucesso."));
    }
}