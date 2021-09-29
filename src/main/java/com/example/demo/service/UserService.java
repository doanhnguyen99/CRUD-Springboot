package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserService {
    List<User> getAll();

    List<User> createUser(UserDTO userDTO);

    Optional<User> findById(Long userId);

    User update(User user);

    void delete(Long id);
}
