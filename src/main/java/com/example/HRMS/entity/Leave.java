package com.example.HRMS.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "leave_applications")
public class Leave{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;

    private String status = "PENDING"; // default status

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)  
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false) 
    private LocalDateTime updatedAt;

    private String appealReason;

    // Getters & Setters
    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public User getEmployee() { 
        return employee; 
    }

    public void setEmployee(User employee) { 
        this.employee = employee; 
    }

    public User getManager() { 
        return manager; 
    }

    public void setManager(User manager) { 
        this.manager = manager; 
    }

    public LocalDate getStartDate() { 
        return startDate; 
    }

    public void setStartDate(LocalDate startDate) { 
        this.startDate = startDate; 
    }

    public LocalDate getEndDate() { 
        return endDate; 
    }

    public void setEndDate(LocalDate endDate) { 
        this.endDate = endDate; 
    }

    private String rejectReason;

    public String getReason() { 
        return reason; 
    }

    public void setReason(String reason) { 
        this.reason = reason; 
    }

    public String getStatus() { 
        return status; 
    }

    public void setStatus(String status) { 
        this.status = status; 
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getRejectReason() { 
        return rejectReason; 
    }

    public void setRejectReason(String rejectReason) { 
        this.rejectReason = rejectReason; 
    }

    public String getAppealReason() { 
        return appealReason; 
    }

    public void setAppealReason(String appealReason) { 
        this.appealReason = appealReason; 
    }
}
