package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.InvalidUserParamException;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/rs/addUser")
    public ResponseEntity register(@RequestBody @Validated User user, BindingResult result) throws InvalidUserParamException {
        if (result.hasErrors())
            throw new InvalidUserParamException();
        int addIndex = userService.addOne(user);
        return ResponseEntity.created(null)
                .header("addIndex", String.valueOf(addIndex)).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUserList() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @PostMapping("/user")
    public ResponseEntity registerUser(@RequestBody @Validated User user, BindingResult result) throws InvalidUserParamException {
        if (result.hasErrors())
            throw new InvalidUserParamException();
        int userId = userService.register(user);
        return ResponseEntity.created(null)
                .header("userId", String.valueOf(userId)).build();
    }

    @GetMapping("/findUserById/{id}")
    public ResponseEntity findUserById(@PathVariable(required = true) int id){
        UserPO user = userService.findUserById(id);
        return ResponseEntity.ok().body(user);
    }
}