package com.connectdepartmentandworkerservice.model.mapper;



import com.connectdepartmentandworkerservice.model.DTO.WorkerDepartmentDTO;
import com.connectdepartmentandworkerservice.model.WorkerDepartment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

    @Mapper
    public interface WorkerDepartmentMapper {

        WorkerDepartmentMapper INSTANCE = Mappers.getMapper(WorkerDepartmentMapper.class);

        WorkerDepartmentDTO workerDepartmentToWorkerDepartmentDTO(WorkerDepartment workerDepartment);
        WorkerDepartment workerDepartmentDTOToWorkerDepartment(WorkerDepartmentDTO workerDepartmentDTO);
    }

