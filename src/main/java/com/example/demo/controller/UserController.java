package com.example.demo.controller;

import com.example.demo.common.Response;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.service.CSVService;
import com.example.demo.service.UserService;
import com.example.demo.util.CSVHelper;
import com.example.demo.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CSVService csvService;

    // get all user
    @GetMapping("/all")
    public List<User> getAll(){
        return userService.getAll();
    }

    // Create user
    @PostMapping("/create")
    public List<User> createUser(@Valid @RequestBody UserDTO userDTO){
       return userService.createUser(userDTO);
    }

    // get user by id
    @GetMapping("/{userId}")
    public ResponseEntity<User> findById(@PathVariable Long userId){
        Optional<User> userOptional = userService.findById(userId);

        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // update user by id
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User user){
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isPresent()){
            Date dateCreated = userOptional.get().getCreatedDate();
            user.setCreatedDate(dateCreated);
        }

        return userOptional.map(user1 -> {
            user.setId(user1.getId());
            return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // delete user by id
    @DeleteMapping("/{userId}")
    public ResponseEntity<Response> deleteUser(@PathVariable Long userId){
        Optional<User> userOptional = userService.findById(userId);

        return userOptional.map(user -> {
            userService.delete(userId);
            return new ResponseEntity<Response>(HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // export data to csv
    @GetMapping("/export-data")
    public ResponseEntity<Resource> getData(){
        String fileName = "users.csv";
        InputStreamResource file = new InputStreamResource(csvService.load());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    // upload data from csv to store db
    @PostMapping("/upload-csv")
    public ResponseEntity<ResponseMessage> uploadFileCSV(@RequestParam("file") MultipartFile file){
        String message = "";

        if (CSVHelper.hasCSVFormat(file)){
            try {
                csvService.save(file);
                message = "Upload file successful: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e){
                message = "Could not upload the file: " + file.getOriginalFilename() + e.getMessage();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }
}
