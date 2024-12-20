package com.workerservice.model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class AnswerWorkerDTO{
        private Long id;
        private String firstname;
        private String lastname;
        private byte[] image;
        private String manager;
        private String status;
        private List<String> departmentNames;
}
