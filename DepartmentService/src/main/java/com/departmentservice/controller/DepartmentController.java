package com.departmentservice.controller;

import com.departmentservice.model.DTO.DepartmentDTO;
import com.departmentservice.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
@Tag(name = "Отдел",
        description = "Почти мной не реализованный но созданный с заделом на последующее расширение, " +
                "под собой подразумевает хранение и обработку данных об отделах")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    @Operation(summary = "Получение данных отделов", description = "Возвращает данные о всех отделах")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.findAll();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение данных отдела", description = "Возвращает данные о конкретном отделе по id")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id) {
        DepartmentDTO department = departmentService.findById(id);
        if (department == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(department);
    }

    @PostMapping
    @Operation(summary = "Добавление отдела", description = "Метод сохраняет данные об отделе в базу данных")
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO createdDepartment = departmentService.saveDepartment(departmentDTO);
        return ResponseEntity.ok(createdDepartment);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление отдела", description = "")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
