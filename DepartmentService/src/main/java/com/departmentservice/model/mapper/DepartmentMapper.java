package com.departmentservice.model.mapper;

import com.departmentservice.model.DTO.DepartmentDTO;
import com.departmentservice.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepartmentMapper {

    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    DepartmentDTO departmentToDepartmentDTO(Department department);
    Department departmentDTOToDepartment(DepartmentDTO departmentDTO);
}