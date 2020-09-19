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

    @PostMapping("/user")
    public ResponseEntity registerUser(@RequestBody @Validated User user, BindingResult result) throws InvalidUserParamException {
        if (result.hasErrors())
            throw new InvalidUserParamException();
        int userId = userService.register(user);
        return ResponseEntity.created(null)
                .header("userId", String.valueOf(userId)).build();
    }

    @GetMapping("/findUserById/{id}")
    public ResponseEntity findUserById(@PathVariable() int id) {
        UserPO user = userService.findUserById(id);
        if (user == null) {
            return ResponseEntity.ok().header("message", "user not found").build();
        }
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity deleteUserById(@PathVariable() int id) {
        if (userService.deleteUserById(id) == id) {
            return ResponseEntity.ok().header("message","user deleted").build();
        }
        return ResponseEntity.ok().header("message", "user not found").build();
    }
}