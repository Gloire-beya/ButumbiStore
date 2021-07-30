package com.butumbi.butumbistoreadmin.user;

import com.butumbi.butumbistorecommon.entity.Role;
import com.butumbi.butumbistorecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    public final static int USERS_PER_PAGE = 4;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> listAllUsers(){
        return (List<User>) userRepository.findAll();
    }

    public Page<User> listByPage(int pageNum, String sortField, String sortOrder, String keyword){
        Sort sort = Sort.by(sortField);
        sort = sortOrder.equals("asc")? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);
        if (keyword != null){
            return userRepository.findAllWithKeyword(keyword, pageable);
        }
        return userRepository.findAll(pageable);
    }

    public List<Role> listAllRoles(){
        return (List<Role>) roleRepository.findAll();
    }

    public User saveUser(User user) {
        boolean isUptadingUser = user.getId() != null;
        if (isUptadingUser){
            Optional<User> existingUser = userRepository.findById(user.getId());
            if (user.getPassword().isEmpty()){
                user.setPassword(existingUser.get().getPassword());
            }else {
                encodePassword(user);
            }
        }else {
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    public User findUserById(Integer id) throws UserNotFoundException{
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) return userRepository.findById(id).get();
        throw new UserNotFoundException("Could not find any user with ID " + id);
    }

    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id, String email){
        User userByEmail = userRepository.findUserByEmail(email);
        if (userByEmail == null) return true;
        boolean isCreateUser = (id == null);
        if (isCreateUser){
            if (userByEmail != null) return false;
        }else {
            if (userByEmail.getId() != id) return false;
        }
        return true;
    }

    public void deleteUserById(Integer id) throws UserNotFoundException{
        Long countById = userRepository.countById(id);
        if (countById == null || countById == 0){
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }
        userRepository.deleteById(id);
    }

    public void updateEnableStatus(Integer id, boolean enabled){
        userRepository.updateEnableStatus(id, enabled);
    }

}
