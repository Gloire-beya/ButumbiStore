package com.butumbi.butumbistoreadmin.user;

import com.butumbi.butumbistorecommon.entity.Role;
import com.butumbi.butumbistorecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    public List<User> listAllUsers(){
        return (List<User>) userRepository.findAll();
    }
    public List<Role> listAllRoles(){
        return (List<Role>) roleRepository.findAll();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
