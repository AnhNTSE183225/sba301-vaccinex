package com.vaccine.repository;

import com.vaccine.entity.Role;
import com.vaccine.enums.EnumRoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role getRoleByRoleName(EnumRoleName role);
}
