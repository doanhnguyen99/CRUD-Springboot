package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CSVService;
import com.example.demo.util.CSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
public class CSVServiceImpl implements CSVService{
    @Autowired
    private UserRepository userRepository;

    public ByteArrayInputStream load(){
        List<User> users = userRepository.findAll();
        ByteArrayInputStream in = CSVHelper.userToCSV(users);
        return in;
    }
}
