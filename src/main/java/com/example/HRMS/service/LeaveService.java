package com.example.HRMS.service;

import com.example.HRMS.entity.Leave;
import com.example.HRMS.repository.LeaveRepository;
import org.springframework.stereotype.Service;
import com.example.HRMS.entity.User;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;

    public LeaveService(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    public List<Leave> getLeavesByEmployee(User employee) {
        return leaveRepository.findByEmployee(employee);
    }

    public List<Leave> getLeavesByManager(Long managerId) {
        return leaveRepository.findByManagerId(managerId);
    }

    public Optional<Leave> getLeaveById(Long id) {
        return leaveRepository.findById(id);
    }

    public Leave saveLeave(Leave leave) {
        return leaveRepository.save(leave);
    }

    public void deleteLeave(Long id) {
        leaveRepository.deleteById(id);
    }

    public List<Leave> getLeavesForCurrentUser(User currentUser) {
        if (currentUser.getRole().getName().equals("ROLE_MANAGER")) {
            // Manager: show leaves where manager_id = currentUser.id
            return leaveRepository.findByManager(currentUser);
        } else if (currentUser.getRole().getName().equals("ROLE_EMPLOYEE")) {
            // Employee: show leaves where employee_id = currentUser.id
            return leaveRepository.findByEmployee(currentUser);
        }
        return Collections.emptyList();
    }

    public void updateStatus(Long id, String status) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid leave ID"));
        leave.setStatus(status);
        leaveRepository.save(leave);
    }

    public void submitAppeal(Long leaveId, String reason, Principal principal) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid leave ID"));
        leave.setAppealReason(reason);
        leave.setStatus("APPEALED");
        leaveRepository.save(leave);
    }
    


}
