package com.butumbi.butumbistoreadmin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    private UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/check_email")
    public String checkDuplicateEmail(Integer id,String email){
        return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }
}
