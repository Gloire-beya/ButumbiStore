package com.butumbi.butumbistoreadmin.user;

import com.butumbi.butumbistorecommon.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Commit
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateFirstRole(){
        Role admin = new Role("Admin", "Manage everything");
        Role savedRole = roleRepository.save(admin);

        assertThat(savedRole.getName()).isEqualTo("Admin");
    }
    @Test
    public void createRoles(){
        Role salesPerson = new Role("Salesperson", "Manage sales report, manage customers, manage orders, update product price, view products, manage shipping rates");
        Role customers = new Role("Customers","Post reviews, view products, view articles, vote reviews, vote questions, manage addresses, checkout, managing shopping cart, view orders");
        Role editor = new Role("Editor", "Mange brands, manage products, mange menus, manage articles, manage categories");
        Role assistant = new Role("Assistant", "Manage questions, manage reviews");
        Role shipper = new Role("Shipper","View orders, update order status, view products");

        List<Role> roles = (List<Role>)roleRepository.saveAll(List.of(salesPerson, customers, editor, assistant, shipper));

        assertThat(roles.size()).isEqualTo(5);
    }
}