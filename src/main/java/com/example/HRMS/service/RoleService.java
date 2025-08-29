package com.example.HRMS.service;
import com.example.HRMS.entity.Role;
import com.example.HRMS.repository.RoleRepository;
import org.springframework.stereotype.Service;  
import java.util.List;
import java.util.Optional;
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) { 
        this.roleRepository = roleRepository; 
    }

    public List<Role> getAllRoles() { 
        return roleRepository.findAll(); 
    }

     public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }
}


