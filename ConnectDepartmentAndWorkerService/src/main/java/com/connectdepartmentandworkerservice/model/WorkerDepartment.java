package com.connectdepartmentandworkerservice.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "worker_department")
public class WorkerDepartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "worker_id", nullable = false)
    private Long workerId;
    @Column(name = "department_id", nullable = false)
    private Long departmentId;
    @Column(name = "deleted", nullable = false)
    private boolean isDeleted = false;

}
