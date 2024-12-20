package com.workerservice.controllers;

import com.workerservice.model.DTO.AnswerWorkerDTO;
import com.workerservice.model.DTO.WorkerDTO;
import com.workerservice.services.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/workers")
@Tag(name = "Работники")
public class WorkerController {
    private final WorkerService workerService;

    @Autowired
    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping("/all")
    public List<AnswerWorkerDTO> getAllWorkers(
            @RequestParam(required = false) String name, //фильтрация по имени сотрудника
            @RequestParam(required = false) List<Long> departmentIds, //фильтр по отделам
            @RequestParam(required = false) String status, //фильтрация по статусу сотрудника
            @RequestParam(defaultValue = "id") String sortField, //поле для сортировки (по умолчанию id)
            @RequestParam(defaultValue = "true") boolean ascending, //порядок сортировки (по умолчанию true для возрастания)
            @RequestParam(defaultValue = "1")  int page, //номер страницы для пагинации (по умолчанию 1).
            @RequestParam(defaultValue = "10") int size) //количество записей на странице (по умолчанию 10)
    {
        return workerService.findAllWithFilters(name, departmentIds, status, sortField, ascending, page, size);
    }


    @PostMapping()
    @Operation(summary = "Создание работника", description = "Позволяет создать нового работника")
    public ResponseEntity<?> createWorker(@Valid @RequestBody  WorkerDTO workerDTO) {
        WorkerDTO savedWorker = workerService.saveWorker(workerDTO);
        if(savedWorker == null) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body("Worker already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedWorker);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Метод удаления работника")
    public ResponseEntity<Void> deleteWorker(@PathVariable("id") @Min(0) Long id) {
        workerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping()
    @Operation(summary = "Сохранение изменений данных о работнике")
    public ResponseEntity<WorkerDTO> updateWorker(@RequestBody @Valid WorkerDTO workerDTO) {
        WorkerDTO updatedWorker = workerService.updateWorker(workerDTO);
        return ResponseEntity.ok(updatedWorker);
    }

    @Operation(summary = "Поиск работника по ID", description = "Позволяет найти работника введя его ID")
    @GetMapping("/{id}")
    public ResponseEntity<AnswerWorkerDTO> findWorkerById(@PathVariable @Min(0) Long id) {
        AnswerWorkerDTO answerWorkerDTO = workerService.findById(id);
        if (answerWorkerDTO != null) {
            return ResponseEntity.ok(answerWorkerDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
