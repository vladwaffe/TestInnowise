package com.connectdepartmentandworkerservice.model.DTO;


import lombok.Data;

@Data
public class WorkerDepartmentDTO {
    private Long id;
    private Long workerId;
    private Long departmentId;
}
