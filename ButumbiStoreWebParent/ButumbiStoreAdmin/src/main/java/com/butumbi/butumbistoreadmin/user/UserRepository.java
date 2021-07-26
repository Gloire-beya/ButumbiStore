package com.butumbi.butumbistoreadmin.user;

import com.butumbi.butumbistorecommon.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("select u from User u where u.email = :email")
    public User findUserByEmail(@Param("email") String email);
}
