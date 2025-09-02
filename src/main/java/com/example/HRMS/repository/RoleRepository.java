package com.example.HRMS.repository;
import com.example.HRMS.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface RoleRepository extends JpaRepository<Role, Long> {
}
