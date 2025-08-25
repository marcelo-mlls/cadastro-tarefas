package com.desafio.userservice.service;



import com.desafio.userservice.model.User;
import com.desafio.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public User createUser(User user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new IllegalArgumentException("Erro email já cadastrado.");
        }
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Usuário não encontrado com id: " + id));
        Optional<User> userWithSameEmail = userRepository.findByEmail(userDetails.getEmail());
        if(userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)){
            throw new IllegalArgumentException("Erro email já cadastrador em outra conta.");
        }
        user.setNome(userDetails.getNome());
        user.setEmail(user.getEmail());
        return userRepository.save(user);
    }

    public void deleteUser(Long id){
        // NOTA: A regra de negócio "Usuários não podem ser deletados se ainda possuírem tarefas associadas"
        // exigiria uma comunicação com o 'task-service'.Não esquecer de arrumar no final
        // Exemplo: if (taskServiceClient.countTasksByUserId(id) > 0) { throw new IllegalStateException("Usuário possui tarefas e não pode ser deletado."); }

        User user = userRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Usuário não encontrado com id: " + id));
        userRepository.delete(user);
    }
}
