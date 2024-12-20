package com.connectdepartmentandworkerservice.controllers;

import com.connectdepartmentandworkerservice.services.ConnectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
@Tag(name = "Сервис соединения", description = "Сервис отвечает за соединения работников и отделов")
public class ConnectController {

    @Autowired
    private ConnectService connectService;

    @GetMapping("/{workerId}")
    @Operation(summary = "Получение данных", description = "Возвращает список id отделов конкретного работника")
    public List<Long> getDepartmentsByWorkerId(@PathVariable Long workerId) {
        return connectService.loadDepartmentIdsForWorker(workerId);
    }

    @GetMapping("/workers")
    @Operation(summary = "Получение данных", description = "Возвращает id рботников по ud отдела")
    public List<Long> getWorkerIdsByDepartmentIds(@RequestParam List<Long> departmentIds) {
        return connectService.getWorkerIdsByDepartmentIds(departmentIds);
    }

    @PostMapping("/{workerId}/{departmentId}")
    @Operation(summary = "Сохранение данных", description = "Сохраняет ассоциацию работник-отдел в таблицу")
    public void createWorkerDepartmentAssociation(@PathVariable Long workerId, @PathVariable Long departmentId) {
        connectService.createWorkerDepartmentAssociation(workerId, departmentId);
    }

    @PutMapping("/{workerId}")
    @Operation(summary = "Изменение данных", description = "Сохраняет измененные данные")
    public void updateWorkerDepartmentAssociations(@PathVariable Long workerId, @RequestBody List<Long> newDepartmentIds) {
        connectService.updateWorkerDepartmentAssociations(workerId, newDepartmentIds);
    }

    @DeleteMapping("/{workerId}/{departmentId}")
    @Operation(summary = "Удаляет данные", description = "Удаляет взаимосвязи")
    public void DeleteWorkerDepartmentAssociation(@PathVariable Long workerId, @PathVariable Long departmentId) {
        connectService.softDeleteWorkerDepartmentAssociation(workerId, departmentId);
    }
}
