package com.butumbi.butumbistoreadmin.user;

import com.butumbi.butumbistorecommon.entity.Role;
import com.butumbi.butumbistorecommon.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Commit
@Slf4j
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void TestCreateUserWithOneRole() {
        User namMHUser = new User("gloire@gmail.com", "glo123", "Glory", "Beya");
        Optional<Role> assistantRole = roleRepository.findById(1);
        if (assistantRole.isPresent()) namMHUser.addRole(assistantRole.get());

        User savedUser = userRepository.save(namMHUser);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void TestCreateUserWithTwoRoles() {
        User gloryUser = new User("sony@gmail.com", "sony123", "Sony", "Tshinguli");
        Optional<Role> adminRole = roleRepository.findById(2);
        Optional<Role> editorRole = roleRepository.findById(3);

        if (adminRole.isPresent()) gloryUser.addRole(adminRole.get());
        if (editorRole.isPresent()) gloryUser.addRole(editorRole.get());

        User savedUser = userRepository.save(gloryUser);
        assertThat(savedUser.getRoleSet().size()).isEqualTo(2);
    }

    @Test
    public void gettingAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        users.stream().forEach(
                user -> {
                    log.info("User No. " + user.getId() + " " + user.getFirstName() + " " + user.getRoleSet());
                }
        );
    }

    @Test
    public void findUserById() {
        Optional<User> optionalUser = userRepository.findById(2);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            log.info("User No. " + user.getId() + " " + user.getFirstName() + " " + user.getRoleSet());
        }
    }

    @Test
    public void updateUserById() {
        Optional<User> optionalUser = userRepository.findById(1);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmail("gloirebeyait@gmail.com");

            User savedUser = userRepository.save(user);
            assertThat(savedUser.getEmail()).isEqualTo("gloirebeyait@gmail.com");
        }
    }

    @Test
    public void updateUserRole() {
        Optional<User> optionalUser = userRepository.findById(2);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.getRoleSet().remove(new Role(2));
            user.addRole(new Role(3), new Role(4));

            User savedUser = userRepository.save(user);

            assertThat(savedUser.getRoleSet().size()).isEqualTo(2);
        }
    }

    @Test
    public void testDeleteUserById() {
        userRepository.deleteById(3);
    }

    @Test
    public void testFindUserByEmail() {
        String email = "gloirebeyait@gmail.com";
        User userByEmail = userRepository.findUserByEmail(email);

        assertThat(userByEmail).isNotNull();
    }

    @Test
    public void testCountUserById() {
        Integer id = 2;
        Long count = userRepository.countById(id);
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void testUpdateDisableStatus() {
        userRepository.updateEnableStatus(2, false);
    }

    @Test
    public void testUpdateEnableStatus() {
        userRepository.updateEnableStatus(9, true);
    }

    @Test
    public void testListFirstPage() {

        int pageNumber = 0;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userRepository.findAll(pageable);
        List<User> userList = page.getContent();

        userList.forEach(user -> System.out.println(user));
        assertThat(userList.size()).isEqualTo(pageSize);

    }

    @Test
    public void findAllWithKeyword() {
        String keyword = "Bruce";
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> allWithKeyword = userRepository.findAllWithKeyword(keyword, pageable);

        List<User> userList = allWithKeyword.getContent();
        System.out.println(userList);
        assertThat(userList.size()).isGreaterThan(0);
    }
}