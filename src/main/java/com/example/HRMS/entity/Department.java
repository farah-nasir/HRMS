package com.example.HRMS.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
@Entity
@Table(name = "departments")

public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    private String description;

    @Column(name = "status", nullable = false)
    private String status = "Active"; //default value

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)  
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false) 
    private LocalDateTime updatedAt;

    @UpdateTimestamp  // auto-update every time entity is updated
    @Column(name = "created_dt")
    private LocalDateTime createdDt;
    

    public Department() {}

    public Department(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getters & setters
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getDescription() { 
        return description; 
    }

    public String getStatus() {
        return status;
    }

    public void setDescription(String description) { 
        this.description = description; 
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
    return createdAt;
}

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedDt() { 
        return createdDt; 
    }

    public void setCreatedDt(LocalDateTime createdDt) { 
        this.createdDt = createdDt; 
    }

}

