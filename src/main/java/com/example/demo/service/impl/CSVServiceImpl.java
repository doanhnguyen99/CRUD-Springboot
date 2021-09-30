package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CSVService;
import com.example.demo.util.CSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVServiceImpl implements CSVService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public ByteArrayInputStream load(){
        List<User> users = userRepository.findAll();
        ByteArrayInputStream in = CSVHelper.userToCSV(users);
        return in;
    }

    @Override
    public void save(MultipartFile file){
        try {
            List<User> users = CSVHelper.uploadCSV(file.getInputStream());
            List<String> listName = new ArrayList<>();
            for (User user : users){
                listName.add(user.getName());
            }

            System.out.println(listName);
            List<User> listUser = userRepository.getUserDuplicate(listName);
            int countUser = listUser.size();
            System.out.println(countUser);
            if (countUser == 0){
                userRepository.saveAll(users);
            }else{
                throw new IOException("Duplicate field name data");
            }

        } catch (IOException e){
            throw new RuntimeException("Fail to store data csv: " + e.getMessage());
        }
    }
}
