package com.example.HRMS.repository;
import com.example.HRMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    List<User> findByRole_Id(Long roleId); 
    Page<User> findByRole_Id(Long roleId, Pageable pageable); 
    // Find a user by id and department id
    Optional<User> findByDepartment_IdAndRole_Id(Long departmentId, Long roleId);
    boolean existsByUsername(String username); 
    
}


