package com.butumbi.butumbistoreadmin.user;

import com.butumbi.butumbistorecommon.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
}
