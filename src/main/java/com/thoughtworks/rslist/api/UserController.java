package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/rs/addUser")
    public ResponseEntity register(@RequestBody @Validated User user){
        int addIndex = userService.addOne(user);
        return ResponseEntity.created(null)
                .header("addIndex", String.valueOf(addIndex)).build();
    }

    @GetMapping("/rs/getUserList")
    public ResponseEntity<List<User>> getUserList(){
        return ResponseEntity.ok().body(userService.getAllUsers());
    }
}
