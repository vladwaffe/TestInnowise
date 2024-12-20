package com.workerservice.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class WorkerDTO {
    private Long id;

    @NotBlank(message = "Firstname is mandatory")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Firstname should not contain numbers")
    private String firstname;

    @NotBlank(message = "Lastname is mandatory")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Lastname should not contain numbers")
    private String lastname;

    @NotBlank(message = "Manager is mandatory")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Lastname should not contain numbers")
    private String manager;

    @NotBlank(message = "Status is mandatory")
    private String status;

    @NotBlank(message = "Department IDs are mandatory")
    @Pattern(regexp = "^[^a-zA-Z ]*$", message = "Lastname should not contain letters")
    private List<Long> departmentIds;
}
