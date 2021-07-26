package com.butumbi.butumbistoreadmin.user;

import com.butumbi.butumbistorecommon.entity.Role;
import com.butumbi.butumbistorecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/users")
    public String listAll(Model model){
        List<User> userList = userService.listAllUsers();
        model.addAttribute("userList", userList);
        return "users";
    }
    @GetMapping("/users/new")
    public String showNewUserForm(Model model){
        User user = new User();
        user.setEnabled(true);
        List<Role> roleList = userService.listAllRoles();

        model.addAttribute("roleList", roleList);
        model.addAttribute("user", user);
        return "user_form";
    }
    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes){
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message","The user has been saved successfully!");
        return "redirect:/users";
    }

}
