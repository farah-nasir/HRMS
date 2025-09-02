package com.example.HRMS.service;

import com.example.HRMS.entity.Leave;
import com.example.HRMS.repository.LeaveRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.HRMS.entity.User;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;

    public LeaveService(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    // public List<Leave> getLeavesByEmployee(User employee) {
    //     return leaveRepository.findByEmployee(employee);
    // }

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

    public Page<Leave> getLeavesForCurrentUser(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return leaveRepository.findByEmployee(user, pageable);
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
    // new method that return Page<Leave>
    public Page <Leave> getPaginatedLeaves(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return leaveRepository.findAll(pageable);
    }


}
